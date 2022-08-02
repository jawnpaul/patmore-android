package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.core.utility.safeNavigate
import com.android.patmore.databinding.FragmentForYouBinding
import com.android.patmore.features.foryou.presentation.adapter.CategoryPagerAdapter
import com.android.patmore.features.foryou.presentation.viewmodel.ForYouViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForYouFragment : Fragment() {
    private lateinit var sportCategoryAdapter: CategoryPagerAdapter
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!
    private val forYouViewModel: ForYouViewModel by activityViewModels()

    private val TAG = ForYouFragment::class.simpleName

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

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
        forYouViewModel.showBottomNavBar(true)

        binding.mainRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forYouViewModel.forYouView.collect { state ->
                    if (state.loading) {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.mainRecyclerView.visibility = View.GONE
                    } else {
                        binding.shimmerLayout.visibility = View.GONE
                        binding.mainRecyclerView.visibility = View.VISIBLE
                    }

                    state.error?.let { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }

                    state.response?.let { sections ->
                        if (sections.isNotEmpty()) {
                            val groupieAdapter = GroupieAdapter()
                            binding.mainRecyclerView.adapter = groupieAdapter
                            groupieAdapter.addAll(sections)
                        } else {
                            val groupieAdapter = GroupieAdapter()
                            binding.mainRecyclerView.adapter = groupieAdapter
                        }
                    }
                }
                /*forYouViewModel.singleTweet.collect { state ->
                    Timber.d(state.toString())
                    when(state.isShown){
                        true -> {
                            Timber.d("True")


                            //forYouViewModel.tweetShown()
                        }
                        false -> {

                        }
                    }

                    state.data?.let {
                        val action = ForYouFragmentDirections.actionForYouFragmentToSingleForYouTweetFragment()
                        findNavController().safeNavigate(action)
                    }
                }*/
            }
        }

        forYouViewModel.selectedTweet.observe(viewLifecycleOwner) {
            when (it.isShown) {
                true -> {
                    val action =
                        ForYouFragmentDirections.actionForYouFragmentToSingleForYouTweetFragment()
                    findNavController().safeNavigate(action)

                    forYouViewModel.tweetShown()
                }
                false -> {
                }
            }
        }

        forYouViewModel.selectedView.observe(viewLifecycleOwner) {
            when (it.isShown) {
                true -> {

                    val action =
                        ForYouFragmentDirections.actionForYouFragmentToAllCategoryTweetFragment()
                    findNavController().safeNavigate(action)

                    forYouViewModel.categoryShown()
                }
                false -> {
                }
            }
        }

        mixPanelUtil.logScreen(TAG)
    }
}
