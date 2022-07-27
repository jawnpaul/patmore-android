package com.android.patmore.features.foryou.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.foryou.domain.model.ForYouTweet
import com.android.patmore.features.foryou.domain.usecases.GetCategoryTweetUseCase
import com.android.patmore.features.foryou.domain.usecases.GetForYouTweetsUseCase
import com.android.patmore.features.foryou.domain.usecases.GetSingleOriginalTweetUseCase
import com.android.patmore.features.foryou.presentation.model.CategoryTweetItem
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation
import com.android.patmore.features.foryou.presentation.model.SingleCategoryTweetItem
import com.android.patmore.features.foryou.presentation.state.ForYouView
import com.android.patmore.features.foryou.presentation.state.SingleTweetView
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

    private val _singleTweet = MutableStateFlow(SingleTweetView())
    val singleTweet: StateFlow<SingleTweetView> = _singleTweet

    private val mutableMap = mutableMapOf<String, String>()

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

            val categoryList = mutableMap.values.distinct().map { category -> category }
            // Adding category here so I can use to filter later on
            val presentation = result.map { aa -> getPresentation(aa.toPresentation()) }

            categoryList.forEach { category ->
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
                section.add(CategoryTweetItem(category, sectionData))
                sections.add(section)
            }

            _forYouView.update {
                it.copy(loading = false, response = sections)
            }
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
}
