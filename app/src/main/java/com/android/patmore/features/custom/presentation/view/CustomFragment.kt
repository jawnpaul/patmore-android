package com.android.patmore.features.custom.presentation.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.patmore.BuildConfig
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.databinding.FragmentCustomBinding
import com.android.patmore.features.authentication.presentation.view.TwitterAuthenticationActivity
import com.android.patmore.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.android.patmore.features.custom.presentation.viewmodel.CustomViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomFragment : Fragment() {
    private var _binding: FragmentCustomBinding? = null
    private val binding get() = _binding!!
    private val TAG = CustomFragment::class.simpleName
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private val customViewModel: CustomViewModel by activityViewModels()

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCustomBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val clientId = BuildConfig.TWITTER_CLIENT_ID
                val callbackUrl = BuildConfig.TWITTER_CALLBACK_URL
                it.data?.let { intent ->
                    viewModel.getOAuth2AccessToken(
                        code = intent.getStringExtra("code")!!,
                        challenge = intent.getStringExtra("challenge")!!,
                        clientId,
                        callbackUrl
                    )
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(requireContext(), TwitterAuthenticationActivity::class.java)
            intent.putExtra("clientId", BuildConfig.TWITTER_CLIENT_ID)
            b.launch(intent)
        }

        if (sharedPreferences.getTwitterUserAccessToken() == null) {
            // show login view
            binding.helperLayout.visibility = View.VISIBLE
        } else {
            // show default
            binding.helperLayout.visibility = View.GONE
        }

        viewModel.hideHelperLayout.observe(viewLifecycleOwner) {
            if (it) {
                // hide layout
                binding.helperLayout.visibility = View.GONE
            } else {
                // show layout
                binding.helperLayout.visibility = View.VISIBLE
            }
        }

        binding.todayChip.setOnClickListener {
            // customViewModel.getUserTimeline()
        }

        viewModel.userGotten.observe(viewLifecycleOwner) {
            if (it == true) {
                // get user timeline
            }

            // unset user gotten when timeline is got
        }

        mixPanelUtil.logScreen(TAG)
    }
}
