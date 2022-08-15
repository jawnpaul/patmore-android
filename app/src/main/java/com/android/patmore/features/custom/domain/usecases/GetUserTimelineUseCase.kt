package com.android.patmore.features.custom.domain.usecases

import com.android.patmore.core.baseusecase.BaseUseCase
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.custom.domain.model.TimelineTweet
import com.android.patmore.features.custom.domain.repository.ICustomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserTimelineUseCase @Inject constructor(private val repository: ICustomRepository) :
    BaseUseCase<GetUserTimelineUseCase.None, List<TimelineTweet>>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, List<TimelineTweet>>> {
        return repository.getUserTimeline()
    }
}
