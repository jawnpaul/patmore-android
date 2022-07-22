package com.android.patmore.features.authentication.domain.repository

import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import kotlinx.coroutines.flow.Flow

interface IAuthenticationRepository {
    suspend fun getUserToken(): Flow<Either<Failure, Unit>>
}
