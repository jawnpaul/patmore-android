package com.android.patmore.features.authentication.data.repository

import com.android.patmore.core.api.PatmoreApiService
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.features.authentication.data.remote.model.CreateTokenRequest
import com.android.patmore.features.authentication.domain.repository.IAuthenticationRepository
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
}
