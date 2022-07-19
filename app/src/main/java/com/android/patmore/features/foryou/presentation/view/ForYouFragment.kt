package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.patmore.databinding.FragmentForYouBinding
import com.android.patmore.features.foryou.presentation.adapter.CategoryPagerAdapter
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation
import com.android.patmore.features.foryou.presentation.viewmodel.ForYouViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForYouFragment : Fragment() {
    private lateinit var sportCategoryAdapter: CategoryPagerAdapter
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!
    private val forYouViewModel: ForYouViewModel by activityViewModels()

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

        forYouViewModel.technologyTweets.observe(viewLifecycleOwner) {
            val fragList = it.map { aa -> getFragment(aa) }
            sportCategoryAdapter =
                CategoryPagerAdapter(
                    this,
                    fragList
                )
            binding.sportPager.adapter = sportCategoryAdapter
        }
    }

    private fun getFragment(forYouTweetPresentation: ForYouTweetPresentation): CategoryFragment {
        return CategoryFragment.newInstance(forYouTweetPresentation)
    }
}
