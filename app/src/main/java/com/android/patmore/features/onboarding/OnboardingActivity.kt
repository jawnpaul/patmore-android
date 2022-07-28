package com.android.patmore.features.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.databinding.ActivityOnboardingBinding
import com.android.patmore.features.authentication.presentation.AuthenticationViewModel
import com.android.patmore.features.home.presentation.view.MainActivity
import com.android.patmore.features.subscription.presentation.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private val categories = mutableSetOf<String>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val TAG = OnboardingActivity::class.simpleName

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val subscriptionViewModel: SubscriptionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel.getToken()

        binding.technologyChip.setOnClickListener {
            enableButton()
            if (categories.contains("technology")) {
                categories.remove("technology")
            } else {
                categories.add("technology")
            }
        }

        binding.businessChip.setOnClickListener {
            enableButton()

            if (categories.contains("business")) {
                categories.remove("business")
            } else {
                categories.add("business")
            }
        }

        binding.sportChip.setOnClickListener {
            enableButton()

            if (categories.contains("sport")) {
                categories.remove("sport")
            } else {
                categories.add("sport")
            }
        }

        binding.musicChip.setOnClickListener {
            enableButton()

            if (categories.contains("music")) {
                categories.remove("music")
            } else {
                categories.add("music")
            }
        }

        binding.travelChip.setOnClickListener {
            enableButton()

            if (categories.contains("travel")) {
                categories.remove("travel")
            } else {
                categories.add("travel")
            }
        }

        binding.animeChip.setOnClickListener {
            enableButton()

            if (categories.contains("anime")) {
                categories.remove("anime")
            } else {
                categories.add("anime")
            }
        }

        binding.onboardingButton.setOnClickListener {
            val list = categories.toList()
            sharedPreferences.saveUserCategories(getCategoriesString(list))
            subscriptionViewModel.subscribeToTopic(list)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscriptionViewModel.uiState.collect { createView ->
                    if (createView.loading) {
                        binding.onboardingButton.setLoading(true)
                    } else {
                        binding.onboardingButton.setLoading(false)
                    }

                    createView.error?.let { error ->
                        Toast.makeText(this@OnboardingActivity, error, Toast.LENGTH_LONG).show()
                        subscriptionViewModel.resetSubscriptionState()
                    }

                    createView.response?.let {
                        sharedPreferences.saveIsFirstInstall(false)
                        // Navigate to main activity
                        val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        subscriptionViewModel.resetSubscriptionState()
                    }
                }
            }
        }

        mixPanelUtil.logScreen(TAG)
    }

    private fun enableButton() {
        val enable =
            binding.technologyChip.isChecked || binding.businessChip.isChecked ||
                binding.sportChip.isChecked || binding.musicChip.isChecked || binding.travelChip.isChecked || binding.animeChip.isChecked
        binding.onboardingButton.isEnabled = enable
    }

    private fun getCategoriesString(ids: List<String>): String {
        var res = ""
        ids.forEach {
            res += " $it"
        }
        return res
    }
}
