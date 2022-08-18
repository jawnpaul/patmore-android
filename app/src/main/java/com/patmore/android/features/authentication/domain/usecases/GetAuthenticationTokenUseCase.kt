package com.patmore.android.features.authentication.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.authentication.domain.repository.IAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthenticationTokenUseCase @Inject constructor(private val repository: IAuthenticationRepository) :
    BaseUseCase<GetAuthenticationTokenUseCase.None, Unit>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, Unit>> {
        return repository.getUserToken()
    }
}
