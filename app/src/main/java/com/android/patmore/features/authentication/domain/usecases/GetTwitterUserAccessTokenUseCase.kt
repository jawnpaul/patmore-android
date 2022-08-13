package com.android.patmore.features.authentication.domain.usecases

import com.android.patmore.core.baseusecase.BaseUseCase
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.authentication.data.remote.model.AccessTokenRequest
import com.android.patmore.features.authentication.domain.repository.IAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTwitterUserAccessTokenUseCase @Inject constructor(private val repository: IAuthenticationRepository) : BaseUseCase<AccessTokenRequest, Unit>() {
    override suspend fun run(params: AccessTokenRequest): Flow<Either<Failure, Unit>> {
        return repository.getOauth2AccessToken(params)
    }
}
