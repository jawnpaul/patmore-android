package com.patmore.android.features.foryou.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.onFailure
import com.patmore.android.core.functional.onSuccess
import com.patmore.android.features.foryou.domain.model.ForYouTweet
import com.patmore.android.features.foryou.domain.usecases.GetCategoryTweetUseCase
import com.patmore.android.features.foryou.domain.usecases.GetForYouTweetsUseCase
import com.patmore.android.features.foryou.domain.usecases.GetSingleOriginalTweetUseCase
import com.patmore.android.features.foryou.presentation.model.CategoryTweetItem
import com.patmore.android.features.foryou.presentation.model.ForYouTweetPresentation
import com.patmore.android.features.foryou.presentation.model.SingleCategoryTweetItem
import com.patmore.android.features.foryou.presentation.state.CategoryTweetView
import com.patmore.android.features.foryou.presentation.state.ForYouView
import com.patmore.android.features.foryou.presentation.state.SingleTweetView
import com.patmore.android.features.foryou.presentation.view.CategoryFragment
import com.xwray.groupie.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val getCategoryTweetUseCase: GetCategoryTweetUseCase,
    private val getSingleOriginalTweetUseCase: GetSingleOriginalTweetUseCase,
    private val getForYouTweetsUseCase: GetForYouTweetsUseCase,
) :
    ViewModel() {

    private val job = Job()

    private val _technologyTweets = MutableLiveData<List<ForYouTweetPresentation>>()
    val technologyTweets get() = _technologyTweets

    private val _forYouView = MutableStateFlow(ForYouView())
    val forYouView: StateFlow<ForYouView> = _forYouView

    private val _selectedTweet = MutableLiveData<SingleTweetView>()
    val selectedTweet get() = _selectedTweet

    private val _selectedView = MutableLiveData<CategoryTweetView>()
    val selectedView get() = _selectedView

    private val _selectedCategory = MutableLiveData<String?>()
    val selectedCategory get() = _selectedCategory

    private val _singleTweet = MutableStateFlow(SingleTweetView())
    val singleTweet: StateFlow<SingleTweetView> = _singleTweet

    private val mutableMap = mutableMapOf<String, String>()

    private val _showBottomNavBar = MutableLiveData<Boolean>()
    val showBottomNav: LiveData<Boolean> get() = _showBottomNavBar

    private val _allPresentation = MutableLiveData<List<ForYouTweetPresentation>>()
    val allPresentation get() = _allPresentation

    private val _selectedCategoryFragment = MutableLiveData<List<CategoryFragment>>()
    val selectedCategoryFragment get() = _selectedCategoryFragment

    private val _currentTweet = MutableLiveData<ForYouTweetPresentation>()
    val currentTweet get() = _currentTweet

    private val _currentPosition = MutableLiveData(0)
    val currentPosition get() = _currentPosition

    fun getTechnologyTweets() {
        getCategoryTweetUseCase(job, "technology") {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { result ->
                // getOriginalTechnologyTweet(result)
            }
        }
    }

    private fun getOriginalTweets(ids: List<String>) {
        val id = getIds(ids)
        getSingleOriginalTweetUseCase(job, id) {
            it.fold(
                ::handleOriginalTweetFailure,
                ::handleOriginalTweetSuccess
            )
        }
    }

    private fun handleOriginalTweetFailure(failure: Failure) {
        Timber.e(failure.toString())
        _forYouView.update {
            it.copy(loading = false, error = "Something went wrong.")
        }
    }

    private fun handleOriginalTweetSuccess(result: List<ForYouTweet>) {
        Timber.d("Tweet Gotten")
        if (result.isNotEmpty()) {
            val sections = arrayListOf<Section>()

            val pp = result.map { aa -> getPresentation(aa.toPresentation()) }.filter { it.mediaList?.isNotEmpty() == true }

            // The idea here is to remove tweets without media
            val cl = pp.map { it.category }.distinct()

            val categoryList = mutableMap.values.distinct().map { category -> category }
            // Adding category here so I can use to filter later on
            val presentation = pp.map { aa -> getPresentation(aa) }

            cl.forEach { category ->
                val section = Section()
                val sectionItems =
                    presentation.filter { tweetPresentation -> tweetPresentation.category == category }
                val sectionData =
                    sectionItems.map { categoryItem ->
                        SingleCategoryTweetItem(
                            categoryItem,
                            onClick = {
                                selectedTweet(it)
                            }
                        )
                    }
                section.add(
                    CategoryTweetItem(
                        category.toString(), sectionData,
                        onClick = {
                            getAllCategory(it)
                        }
                    )
                )
                sections.add(section)
            }

            _forYouView.update {
                it.copy(loading = false, response = sections)
            }

            _allPresentation.value = presentation
        } else {
            _forYouView.update {
                it.copy(loading = false, response = emptyList())
            }
        }
    }

    private fun getIds(ids: List<String>): String {
        var res = ""
        ids.forEach {
            res += ",$it"
        }
        res = res.removePrefix(",")
        return res
    }

    fun getForYouTweets() {
        _forYouView.update {
            it.copy(loading = true)
        }
        getForYouTweetsUseCase(job, GetForYouTweetsUseCase.None()) {
            it.fold(
                ::handleForYouFailure,
                ::handleForYouSuccess
            )
        }
    }

    private fun handleForYouFailure(failure: Failure) {
        _forYouView.update {
            it.copy(loading = false, error = "Something went wrong.")
        }
    }

    private fun handleForYouSuccess(response: List<Pair<String, List<String>>>) {
        val ids = response.flatMap { pair -> pair.second.map { it } }
        // make call to twitter api
        getOriginalTweets(ids)

        response.forEach { pair ->
            val ab = pair.second.associateWith { pair.first }
            mutableMap += ab
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private fun getPresentation(presentation: ForYouTweetPresentation): ForYouTweetPresentation {
        return presentation.copy(category = mutableMap[presentation.id])
    }

    private fun selectedTweet(forYouTweetPresentation: ForYouTweetPresentation) {
        _selectedTweet.value = SingleTweetView(isShown = true, data = forYouTweetPresentation)
    }

    fun tweetShown() {
        _selectedTweet.value = selectedTweet.value?.copy(isShown = false)
    }

    fun showBottomNavBar(show: Boolean) {
        _showBottomNavBar.value = show
    }

    private fun getAllCategory(category: String) {
        _selectedCategory.value = category
        _allPresentation.value?.let {
            val matches = it.filter { aa -> aa.category == category }
            val fragments = matches.map { aa -> getFragment(aa) }
            _selectedCategoryFragment.value = fragments

            _selectedView.value = CategoryTweetView(isShown = true, data = matches)

            setCurrentTweet(matches[0])
            // setCurrentPosition(0)
        }
    }

    fun categoryShown() {
        _selectedView.value = selectedView.value?.copy(isShown = false)
    }

    fun setCurrentTweet(tweetPresentation: ForYouTweetPresentation) {
        _currentTweet.value = tweetPresentation
    }

    fun setCurrentPosition(value: Int) {
        _currentPosition.value = value
    }

    private fun getFragment(forYouTweetPresentation: ForYouTweetPresentation): CategoryFragment {
        val bundle = Bundle()
        bundle.apply {
            putParcelable("param1", forYouTweetPresentation)
        }
        val fragment = CategoryFragment().apply {
            arguments = bundle
        }
        return fragment
    }
}
