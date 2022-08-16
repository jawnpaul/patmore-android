package com.patmore.android.features.custom.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.onFailure
import com.patmore.android.core.functional.onSuccess
import com.patmore.android.features.custom.domain.usecases.GetUserTimelineUseCase
import com.patmore.android.features.custom.presentation.state.CustomTimelineView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomViewModel @Inject constructor(private val getUserTimelineUseCase: GetUserTimelineUseCase) :
    ViewModel() {
    private val job = Job()

    private val _customTimeline = MutableLiveData<CustomTimelineView>()
    val customTimeline get() = _customTimeline

    fun getUserTimeline() {
        _customTimeline.value = CustomTimelineView(loading = true)
        getUserTimelineUseCase(job, GetUserTimelineUseCase.None()) {
            it.onSuccess { res ->
                Timber.d("Timeline gotten")
                _customTimeline.value = CustomTimelineView(
                    loading = false,
                    response = res.map { aa -> aa.toPresentation() }
                )
            }
            it.onFailure { failure ->
                Timber.e(failure.toString())
                when (failure) {
                    is Failure.UnAuthorizedError -> {
                        _customTimeline.value =
                            CustomTimelineView(loading = false, error = "Auth failed")
                    }
                    else -> {
                        Timber.e(failure.toString())
                        _customTimeline.value =
                            CustomTimelineView(loading = false, error = "Something went wrong.")
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
