package com.patmore.android.features.authentication.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.authentication.data.remote.model.AccessTokenRequest
import com.patmore.android.features.authentication.domain.repository.IAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTwitterUserAccessTokenUseCase @Inject constructor(private val repository: IAuthenticationRepository) : BaseUseCase<AccessTokenRequest, Unit>() {
    override suspend fun run(params: AccessTokenRequest): Flow<Either<Failure, Unit>> {
        return repository.getOauth2AccessToken(params)
    }
}
