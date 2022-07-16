package com.android.patmore.features.foryou.domain.repository

import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.foryou.domain.model.ForYouTweet
import kotlinx.coroutines.flow.Flow

interface IForYouRepository {

    suspend fun getCategoryTweets(category: String): Flow<Either<Failure, List<ForYouTweet>>>
}
