package com.android.patmore.features.custom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.custom.domain.usecases.GetUserTimelineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomViewModel @Inject constructor(private val getUserTimelineUseCase: GetUserTimelineUseCase) :
    ViewModel() {
    private val job = Job()

    fun getUserTimeline() {
        getUserTimelineUseCase(job, GetUserTimelineUseCase.None()) {
            it.onSuccess { Timber.d("Timeline gotten") }
            it.onFailure { failure ->
                when (failure) {
                    is Failure.UnAuthorizedError -> {
                        // Refresh token
                        Timber.e("Token has expired")
                    }
                    else -> {
                        Timber.e(failure.toString())
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
