package com.patmore.android.features.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.core.utility.analytics.MixPanelUtil
import com.patmore.android.databinding.ActivityOnboardingBinding
import com.patmore.android.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.patmore.android.features.home.presentation.view.MainActivity
import com.patmore.android.features.subscription.presentation.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
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

        binding.scienceChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("science")
            } else {
                // add to set
                categories.add("science")
            }
            enableButton()
        }

        binding.familyChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("family and relationships")
            } else {
                // add to set
                categories.add("family and relationships")
            }
            enableButton()
        }

        binding.fashionChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("fashion and beauty")
            } else {
                // add to set
                categories.add("fashion and beauty")
            }
            enableButton()
        }

        binding.foodChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("food")
            } else {
                // add to set
                categories.add("food")
            }
            enableButton()
        }

        binding.fitnessChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("fitness")
            } else {
                // add to set
                categories.add("fitness")
            }
            enableButton()
        }

        binding.technologyChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isEmpty()) {
                categories.remove("technology")
            } else {
                categories.add("technology")
            }
            enableButton()
        }

        binding.businessChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isEmpty()) {
                categories.remove("business")
            } else {
                categories.add("business")
            }
            enableButton()
        }

        binding.sportChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isEmpty()) {
                categories.remove("sports")
            } else {
                categories.add("sports")
            }
            enableButton()
        }

        binding.musicChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isEmpty()) {
                categories.remove("music")
            } else {
                categories.add("music")
            }
            enableButton()
        }

        binding.travelChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                categories.remove("travel")
            } else {
                categories.add("travel")
            }
            enableButton()
        }

        binding.animeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("animation and comics")
            } else {
                // add to set
                categories.add("animation and comics")
            }
            enableButton()
        }

        binding.artsChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("arts and culture")
            } else {
                // add to set
                categories.add("arts and culture")
            }
            enableButton()
        }

        binding.careersChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                // nothing selected in group
                // remove from set
                categories.remove("careers")
            } else {
                // add to set
                categories.add("careers")
            }
            enableButton()
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
        binding.onboardingButton.isEnabled = categories.isNotEmpty()
    }

    private fun getCategoriesString(ids: List<String>): String {
        var res = ""
        ids.forEach {
            res += " $it"
        }
        return res
    }
}
