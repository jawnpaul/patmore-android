package com.patmore.android.features.custom.domain.repository

import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.custom.domain.model.TimelineTweet
import kotlinx.coroutines.flow.Flow

interface ICustomRepository {

    suspend fun getUserTimeline(): Flow<Either<Failure, List<TimelineTweet>>>

    suspend fun getCurrentUser(): Flow<Either<Failure, Unit>>
}
