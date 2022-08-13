package com.android.patmore.features.authentication.data.repository

import com.android.patmore.BuildConfig
import com.android.patmore.core.api.PatmoreApiService
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.features.authentication.data.remote.model.AccessTokenRequest
import com.android.patmore.features.authentication.data.remote.model.CreateTokenRequest
import com.android.patmore.features.authentication.data.remote.model.Oauth1AccessTokenRequest
import com.android.patmore.features.authentication.domain.repository.IAuthenticationRepository
import com.tycz.tweedle.lib.api.Response
import com.tycz.tweedle.lib.authentication.Authentication1
import com.tycz.tweedle.lib.authentication.Authentication2
import com.tycz.tweedle.lib.authentication.oauth.OAuth1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val apiService: PatmoreApiService,
) : IAuthenticationRepository {

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    override suspend fun getUserToken(): Flow<Either<Failure, Unit>> = flow {
        if (sharedPreferences.getAccessToken() == null) {
            try {
                val id = UUID.randomUUID().toString()
                val req = CreateTokenRequest(id = id)
                val res = apiService.generateToken(req)
                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let {
                            val token = it.token
                            sharedPreferences.saveAccessToken(token)
                            emit(Either.Right(Unit))
                            mixPanelUtil.identifyUser(id)
                            mixPanelUtil.profileUpdate(id)
                        }
                    }
                    false -> {
                        when {
                            res.code() == 404 -> {
                                Timber.d("404 error")
                                emit(Either.Left(Failure.UnAuthorizedError))
                            }
                            res.code() == 400 -> {
                                emit(Either.Left(Failure.BadRequest))
                            }
                            else -> {
                                emit(Either.Left(Failure.ServerError))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.stackTraceToString())
                emit(Either.Left(Failure.ServerError))
            }
        } else {
            Timber.e("Access token is available")
        }
    }

    override suspend fun getOauth2AccessToken(request: AccessTokenRequest): Flow<Either<Failure, Unit>> =
        flow {
            val authentication = Authentication2(request.code, request.clientID)
            val response = authentication.getAccessToken(request.callback, request.challenge)

            if (response is Response.Success) {
                response.data?.let {
                    sharedPreferences.saveTwitterUserAccessToken(it.access_token)
                    sharedPreferences.saveTokenExpiration(it.expires_in * 1000L)
                    it.refresh_token?.let { refresh ->
                        sharedPreferences.saveTwitterUserRefreshToken(refresh)
                    }
                }
                emit(Either.Right(Unit))
            } else {
                emit(Either.Left(Failure.ServerError))
            }
        }

    override suspend fun getOauth1AccessToken(request: Oauth1AccessTokenRequest): Flow<Either<Failure, Unit>> =
        flow {
            val oauth1 = OAuth1(
                key = BuildConfig.TWITTER_API_KEY,
                secret = BuildConfig.TWITTER_API_SECRET,
                oAuthToken = null,
                oAuthSecret = null
            )
            val authentication = Authentication1(oauth1)
            val response = authentication.getAccessToken(request.oAuthToken, request.oAuthVerifier)
            if (response.error == null) {
                val a = response.oauthToken
                val b = response.oauthTokenSecret

                if (a != null) {
                    sharedPreferences.saveOAuthToken(a)
                }
                if (b != null) {
                    sharedPreferences.saveOAuthSecret(b)
                }
            }
        }
}
