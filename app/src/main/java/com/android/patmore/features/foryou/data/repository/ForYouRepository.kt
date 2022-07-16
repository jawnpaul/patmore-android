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
                val apiCall = api.getCategory(category)
                if (apiCall.isSuccessful) {
                    Timber.d("Successful")
                    emit(Either.Right(listOf(ForYouTweet(2L))))
                }
            } catch (e: Exception) {
                Timber.e(e.stackTraceToString())
                emit(Either.Left(Failure.ServerError))
            }
        }
}
