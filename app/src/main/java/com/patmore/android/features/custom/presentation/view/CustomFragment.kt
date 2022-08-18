package com.patmore.android.features.custom.presentation.view

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.patmore.android.BuildConfig
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.core.utility.analytics.MixPanelUtil
import com.patmore.android.core.utility.initRecyclerViewWithLineDecoration
import com.patmore.android.databinding.FragmentCustomBinding
import com.patmore.android.features.authentication.presentation.view.TwitterAuthenticationActivity
import com.patmore.android.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.patmore.android.features.custom.presentation.adapter.TwitterTimelineAdapter
import com.patmore.android.features.custom.presentation.viewmodel.CustomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                lifecycleScope.launch {
                    delay(1000L)
                    customViewModel.getUserTimeline()
                }
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
                timelineAdapter.submitList(it.response)

                // customViewModel.getUserFollowers()
            }

            if (it.error != null) {
                Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
            }
        }

        mixPanelUtil.logScreen(TAG)
    }

    private fun setUpListAdapter() {
        timelineAdapter = TwitterTimelineAdapter { pair ->
            val url = "https://twitter.com/${pair.first}/status/${pair.second}"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.twitter.android")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )
                )
            }
        }
        context?.let {
            binding.tweetsRecyclerView.initRecyclerViewWithLineDecoration(it)
        }
        binding.tweetsRecyclerView.adapter = timelineAdapter
    }
}
