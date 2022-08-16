package com.patmore.android.features.subscription.data.repository

import com.patmore.android.core.api.PatmoreApiService
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.patmore.android.features.subscription.domain.repository.ISubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SubscriptionRepository @Inject constructor(private val apiService: PatmoreApiService) :
    ISubscriptionRepository {
    override suspend fun subscribeToTopic(request: CreateSubscriptionRequest): Flow<Either<Failure, String>> =
        flow {
            try {
                val res = apiService.subscribeUser(request)
                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let {
                            emit(Either.Right(it.message))
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
                            res.code() == 401 -> {
                                Timber.e("401 error")
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
        }
}
