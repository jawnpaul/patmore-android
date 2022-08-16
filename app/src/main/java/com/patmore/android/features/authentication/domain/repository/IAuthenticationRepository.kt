package com.patmore.android.features.authentication.domain.repository

import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.authentication.data.remote.model.AccessTokenRequest
import kotlinx.coroutines.flow.Flow

interface IAuthenticationRepository {
    suspend fun getUserToken(): Flow<Either<Failure, Unit>>

    suspend fun getOauth2AccessToken(request: AccessTokenRequest): Flow<Either<Failure, Unit>>
}
