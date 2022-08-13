package com.android.patmore.features.authentication.domain.repository

import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.authentication.data.remote.model.AccessTokenRequest
import com.android.patmore.features.authentication.data.remote.model.Oauth1AccessTokenRequest
import kotlinx.coroutines.flow.Flow

interface IAuthenticationRepository {
    suspend fun getUserToken(): Flow<Either<Failure, Unit>>

    suspend fun getOauth2AccessToken(request: AccessTokenRequest): Flow<Either<Failure, Unit>>

    suspend fun getOauth1AccessToken(request: Oauth1AccessTokenRequest): Flow<Either<Failure, Unit>>
}
