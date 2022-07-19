package com.android.patmore.features.foryou.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.foryou.domain.usecases.GetCategoryTweetUseCase
import com.android.patmore.features.foryou.domain.usecases.GetSingleOriginalTweetUseCase
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val getCategoryTweetUseCase: GetCategoryTweetUseCase,
    private val getSingleOriginalTweetUseCase: GetSingleOriginalTweetUseCase,
) :
    ViewModel() {

    private val job = Job()

    private val _technologyTweets = MutableLiveData<List<ForYouTweetPresentation>>()
    val technologyTweets get() = _technologyTweets

    fun getTechnologyTweets() {
        getCategoryTweetUseCase(job, "technology") {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { result ->
                getOriginalTechnologyTweet(result)
            }
        }
    }

    private fun getOriginalTechnologyTweet(ids: List<String>) {
        val id = getIds(ids)
        getSingleOriginalTweetUseCase(job, id) {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { result ->
                Timber.d("Tweet Gotten")
                _technologyTweets.value = result.map { aa -> aa.toPresentation() }
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
