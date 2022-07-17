package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.patmore.databinding.FragmentForYouBinding
import com.android.patmore.features.foryou.presentation.adapter.CategoryPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForYouFragment : Fragment() {
    private lateinit var sportCategoryAdapter: CategoryPagerAdapter
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentForYouBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sportCategoryAdapter =
            CategoryPagerAdapter(
                this,
                listOf(
                    CategoryFragment.newInstance("Ade", "1 day"),
                    CategoryFragment.newInstance("Paulo", "2 mins")
                )
            )
        binding.sportPager.adapter = sportCategoryAdapter
    }
}
