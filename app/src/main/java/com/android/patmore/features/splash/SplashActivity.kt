package com.android.patmore.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.android.patmore.R
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.features.home.presentation.view.MainActivity
import com.android.patmore.features.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashTimeout = 2000L

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (sharedPreferences.getIsFirstInstall()) {
                    startActivity(Intent(this, OnboardingActivity::class.java))
                    sharedPreferences.saveIsFirstInstall(false)
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            },
            splashTimeout
        )
    }
}
