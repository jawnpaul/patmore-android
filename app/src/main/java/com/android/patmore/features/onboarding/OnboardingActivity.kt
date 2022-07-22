package com.android.patmore.features.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.patmore.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // TODO:Have a button that leads to main activity
        // TODO:Token generation should happen here
    }
}
