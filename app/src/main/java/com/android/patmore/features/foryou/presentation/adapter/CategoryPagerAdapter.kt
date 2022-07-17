package com.android.patmore.features.foryou.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.patmore.features.foryou.presentation.view.CategoryFragment

class CategoryPagerAdapter(fragment: Fragment, val list: List<CategoryFragment>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}
