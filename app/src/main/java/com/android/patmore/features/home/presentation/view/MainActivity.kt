package com.android.patmore.features.home.presentation.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.patmore.R
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.databinding.ActivityMainBinding
import com.android.patmore.features.custom.presentation.viewmodel.CustomViewModel
import com.android.patmore.features.foryou.presentation.viewmodel.ForYouViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val forYouViewModel: ForYouViewModel by viewModels()
    private val customViewModel: CustomViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val TAG = MainActivity::class.simpleName

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        navView.setupWithNavController(navController)

        forYouViewModel.showBottomNav.observe(
            this
        ) {
            if (it) {
                binding.navView.visibility = View.VISIBLE
            } else {
                binding.navView.visibility = View.GONE
            }
        }

        customViewModel.getUserTimeline()
        // forYouViewModel.getTechnologyTweets()
        forYouViewModel.getForYouTweets()

        mixPanelUtil.logScreen(TAG)
    }
}
