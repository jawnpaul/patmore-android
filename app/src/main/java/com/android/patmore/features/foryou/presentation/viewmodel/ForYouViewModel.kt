package com.android.patmore.features.foryou.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.foryou.domain.usecases.GetCategoryTweetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(private val getCategoryTweetUseCase: GetCategoryTweetUseCase) :
    ViewModel() {

    private val job = Job()

    fun getTechnologyTweets() {
        getCategoryTweetUseCase(job, "technology") {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { Timber.d("We have a success") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
