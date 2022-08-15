package com.android.patmore.features.custom.domain.repository

import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.custom.domain.model.TimelineTweet
import kotlinx.coroutines.flow.Flow

interface ICustomRepository {

    suspend fun getUserTimeline(): Flow<Either<Failure, List<TimelineTweet>>>

    suspend fun getCurrentUser(): Flow<Either<Failure, Unit>>
}
