package com.android.patmore.features.authentication.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.authentication.data.remote.model.AccessTokenRequest
import com.android.patmore.features.authentication.domain.usecases.GetAuthenticationTokenUseCase
import com.android.patmore.features.authentication.domain.usecases.GetTwitterUserAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationTokenUseCase: GetAuthenticationTokenUseCase,
    private val getTwitterUserAccessTokenUseCase: GetTwitterUserAccessTokenUseCase,
) :
    ViewModel() {

    private val job = Job()

    private val _hideHelperLayout = MutableLiveData<Boolean>()
    val hideHelperLayout get() = _hideHelperLayout

    fun getToken() {
        authenticationTokenUseCase(job, GetAuthenticationTokenUseCase.None()) {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess {
                Timber.d("Token generated")
                // make call to subscribe user
            }
        }
    }

    fun getOAuth2AccessToken(
        code: String,
        challenge: String,
        clientId: String,
        callbackUrl: String,
    ) {
        val request = AccessTokenRequest(
            code = code,
            challenge = challenge,
            clientID = clientId,
            callback = callbackUrl
        )

        getTwitterUserAccessTokenUseCase(job, request) {
            it.onSuccess {
                _hideHelperLayout.value = true
                Timber.d("Success")
                // get user timeline
            }
            it.onFailure { Timber.e("Failure") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
