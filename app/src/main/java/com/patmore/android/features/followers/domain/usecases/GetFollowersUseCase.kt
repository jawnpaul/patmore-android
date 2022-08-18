package com.patmore.android.features.followers.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.followers.domain.repository.IFollowersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFollowersUseCase @Inject constructor(private val repository: IFollowersRepository) :
    BaseUseCase<GetFollowersUseCase.None, Unit>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, Unit>> {
        return repository.getUserFollowers()
    }
}
