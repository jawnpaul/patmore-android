package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.patmore.R
import com.android.patmore.core.utility.capitalizeFirstLetter
import com.android.patmore.features.foryou.presentation.adapter.CategoryPagerAdapter
import com.android.patmore.features.foryou.presentation.viewmodel.ForYouViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCategoryTweetFragment : Fragment() {

    private lateinit var categoryPagerAdapter: CategoryPagerAdapter
    private lateinit var viewPager: ViewPager2

    private val forYouViewModel: ForYouViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_category_tweet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forYouViewModel.showBottomNavBar(false)

        viewPager = view.findViewById(R.id.pager)

        val toolbarTitle: TextView = view.findViewById(R.id.all_toolbar_title)
        val toolbar: MaterialToolbar = view.findViewById(R.id.all_toolbar)

        forYouViewModel.selectedCategory.observe(viewLifecycleOwner) {
            toolbarTitle.text = it?.lowercase()?.capitalizeFirstLetter()
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        forYouViewModel.selectedCategoryFragment.observe(viewLifecycleOwner) {
            categoryPagerAdapter = CategoryPagerAdapter(this, it)

            viewPager.adapter = categoryPagerAdapter
        }
    }
}
