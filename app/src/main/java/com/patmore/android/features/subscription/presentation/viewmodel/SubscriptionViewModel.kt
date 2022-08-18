package com.patmore.android.features.subscription.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.patmore.android.core.exception.Failure
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.patmore.android.features.subscription.domain.usecases.CreateSubscriptionUseCase
import com.patmore.android.features.subscription.presentation.state.CreateSubscriptionView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(private val createSubscriptionUseCase: CreateSubscriptionUseCase) :
    ViewModel() {

    private val job = Job()

    private val _uiState = MutableStateFlow(CreateSubscriptionView())
    val uiState: StateFlow<CreateSubscriptionView> = _uiState

    fun subscribeToTopic(topics: List<String>) {
        val request = CreateSubscriptionRequest(categories = topics)
        _uiState.update { currentState ->
            currentState.copy(loading = true)
        }
        createSubscriptionUseCase(job, request) {
            it.fold(
                ::handleSubscriptionFailure,
                ::handleSubscriptionSuccess
            )
        }
    }

    private fun handleSubscriptionFailure(failure: Failure) {
        Timber.e(failure.toString())
        _uiState.update { currentState ->
            currentState.copy(loading = false, error = "Something went wrong.")
        }
    }

    private fun handleSubscriptionSuccess(response: String) {
        _uiState.update { currentState ->
            currentState.copy(loading = false, response = response)
        }
    }

    fun resetSubscriptionState() {
        _uiState.update { currentUiState ->
            currentUiState.copy(response = null, error = null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
