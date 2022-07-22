package com.android.patmore.features.authentication.domain.usecases

import com.android.patmore.core.baseusecase.BaseUseCase
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.authentication.domain.repository.IAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthenticationTokenUseCase @Inject constructor(private val repository: IAuthenticationRepository) :
    BaseUseCase<GetAuthenticationTokenUseCase.None, Unit>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, Unit>> {
        return repository.getUserToken()
    }
}
