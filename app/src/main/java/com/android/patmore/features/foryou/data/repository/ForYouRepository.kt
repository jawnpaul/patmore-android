package com.android.patmore.features.foryou.data.repository

import com.android.patmore.core.api.PatmoreApiService
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.foryou.domain.model.ForYouTweet
import com.android.patmore.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ForYouRepository @Inject constructor(private val api: PatmoreApiService) : IForYouRepository {
    override suspend fun getCategoryTweets(category: String): Flow<Either<Failure, List<ForYouTweet>>> =
        flow {
            try {
                val res = api.getCategory(category)
                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let { it ->
                            emit(Either.Right(it.response.map { aa -> aa.toForYouTweet() }))
                        } ?: Either.Left(Failure.DataError)
                    }
                    false -> {
                        when {
                            res.code() == 404 -> {
                                Timber.d("404 error")
                                Either.Left(Failure.UnAuthorizedError)
                            }
                            res.code() == 400 -> {
                                Either.Left(Failure.BadRequest)
                            }
                            else -> {
                                Either.Left(Failure.ServerError)
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
