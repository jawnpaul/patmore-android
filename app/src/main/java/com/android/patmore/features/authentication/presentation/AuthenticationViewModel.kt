package com.android.patmore.features.authentication.presentation

import androidx.lifecycle.ViewModel
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.authentication.domain.usecases.GetAuthenticationTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val authenticationTokenUseCase: GetAuthenticationTokenUseCase) :
    ViewModel() {

    private val job = Job()

    fun getToken() {
        authenticationTokenUseCase(job, GetAuthenticationTokenUseCase.None()) {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { Timber.d("Token generated")
            // make call to subscribe user

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
