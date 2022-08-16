package com.patmore.android.features.authentication.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.onFailure
import com.patmore.android.core.functional.onSuccess
import com.patmore.android.features.authentication.data.remote.model.AccessTokenRequest
import com.patmore.android.features.authentication.data.remote.model.Oauth1AccessTokenRequest
import com.patmore.android.features.authentication.domain.usecases.GetAuthenticationTokenUseCase
import com.patmore.android.features.authentication.domain.usecases.GetTwitterUserAccessTokenUseCase
import com.patmore.android.features.custom.domain.usecases.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationTokenUseCase: GetAuthenticationTokenUseCase,
    private val getTwitterUserAccessTokenUseCase: GetTwitterUserAccessTokenUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) :
    ViewModel() {

    private val job = Job()

    private val _hideHelperLayout = MutableLiveData<Boolean>()
    val hideHelperLayout get() = _hideHelperLayout

    private val _userGotten = MutableLiveData<Boolean?>()
    val userGotten get() = _userGotten

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
                // get current user
                getCurrentUser()
                // get user timeline
            }
            it.onFailure { Timber.e("Failure") }
        }
    }

    fun getOAuth1AccessToken(token: String, verifier: String) {
        val request = Oauth1AccessTokenRequest(oAuthToken = token, oAuthVerifier = verifier)
    }

    private fun getCurrentUser() {
        getCurrentUserUseCase(job, GetCurrentUserUseCase.None()) {
            it.onFailure { failure ->
                when (failure) {
                    is Failure.UnAuthorizedError -> {
                        Timber.e("Token has expired")
                    }
                    else -> {
                        Timber.e(failure.toString())
                    }
                }
            }

            it.onSuccess {
                _userGotten.value = true
            }
        }
    }

    fun unSetUserGotten() {
        _userGotten.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
