package com.android.patmore.features.foryou.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.foryou.domain.model.ForYouTweet
import com.android.patmore.features.foryou.domain.usecases.GetCategoryTweetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(private val getCategoryTweetUseCase: GetCategoryTweetUseCase) :
    ViewModel() {

    private val job = Job()

    private val _technologyTweets = MutableLiveData<List<ForYouTweet>>()
    val technologyTweets get() = _technologyTweets

    fun getTechnologyTweets() {
        getCategoryTweetUseCase(job, "technology") {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { result ->
                val ids = result.map { aa -> aa.id }
                getTechnologyOriginalTweet(ids)
            }
        }
    }

    private fun getTechnologyOriginalTweet(ids: List<Long>) {
        // make twitter api call
        ids.forEach {
            Timber.d(it.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
