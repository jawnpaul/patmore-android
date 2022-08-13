package com.android.patmore.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.patmore.R
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.android.patmore.features.home.presentation.view.MainActivity
import com.android.patmore.features.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashTimeout = 2000L

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private val TAG = SplashActivity::class.simpleName

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        authenticationViewModel.getToken()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (sharedPreferences.getIsFirstInstall()) {
                    startActivity(Intent(this, OnboardingActivity::class.java))

                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            },
            splashTimeout
        )

        mixPanelUtil.logScreen(TAG)
    }
}
