package com.patmore.android.features.custom.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.custom.domain.repository.ICustomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val repository: ICustomRepository) :
    BaseUseCase<GetCurrentUserUseCase.None, Unit>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, Unit>> {
        return repository.getCurrentUser()
    }
}
