package com.android.patmore.features.custom.presentation.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.patmore.BuildConfig
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.core.utility.initRecyclerViewWithLineDecoration
import com.android.patmore.databinding.FragmentCustomBinding
import com.android.patmore.features.authentication.presentation.view.TwitterAuthenticationActivity
import com.android.patmore.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.android.patmore.features.custom.presentation.adapter.TwitterTimelineAdapter
import com.android.patmore.features.custom.presentation.viewmodel.CustomViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CustomFragment : Fragment() {
    private var _binding: FragmentCustomBinding? = null
    private val binding get() = _binding!!
    private val TAG = CustomFragment::class.simpleName
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private val customViewModel: CustomViewModel by activityViewModels()
    private lateinit var timelineAdapter: TwitterTimelineAdapter

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

        setUpListAdapter()

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
                customViewModel.getUserTimeline()
            }
        }

        customViewModel.customTimeline.observe(viewLifecycleOwner) {
            if (it.loading) {
                // show shimmer
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.tweetsRecyclerView.visibility = View.GONE
            } else {
                // hide shimmer
                binding.shimmerLayout.visibility = View.GONE
                binding.tweetsRecyclerView.visibility = View.VISIBLE
            }

            if (it.response != null) {
                viewModel.unSetUserGotten()
                Timber.e(it.response.size.toString())
                timelineAdapter.submitList(it.response)
            }

            if (it.error != null) {
                Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
            }
        }

        mixPanelUtil.logScreen(TAG)
    }

    private fun setUpListAdapter() {
        timelineAdapter = TwitterTimelineAdapter { id ->
        }
        context?.let {
            binding.tweetsRecyclerView.initRecyclerViewWithLineDecoration(it)
        }
        binding.tweetsRecyclerView.adapter = timelineAdapter
    }
}
