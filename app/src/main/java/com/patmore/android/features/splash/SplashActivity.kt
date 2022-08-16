package com.patmore.android.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.patmore.android.R
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.core.utility.analytics.MixPanelUtil
import com.patmore.android.features.authentication.presentation.viewmodel.AuthenticationViewModel
import com.patmore.android.features.home.presentation.view.MainActivity
import com.patmore.android.features.onboarding.OnboardingActivity
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
