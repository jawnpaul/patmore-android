package com.patmore.android.features.authentication.data.repository

import com.patmore.android.core.api.PatmoreApiService
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.core.utility.analytics.MixPanelUtil
import com.patmore.android.features.authentication.data.remote.model.AccessTokenRequest
import com.patmore.android.features.authentication.data.remote.model.CreateTokenRequest
import com.patmore.android.features.authentication.domain.repository.IAuthenticationRepository
import com.tycz.tweedle.lib.api.Response
import com.tycz.tweedle.lib.authentication.Authentication2
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
}
