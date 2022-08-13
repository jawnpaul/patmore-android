package com.android.patmore.features.custom.domain.repository

import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import kotlinx.coroutines.flow.Flow

interface ICustomRepository {

    suspend fun getUserTimeline(): Flow<Either<Failure, Unit>>

    suspend fun getCurrentUser(): Flow<Either<Failure, Unit>>

    suspend fun getTimeline(): Flow<Either<Failure, Unit>>
}
