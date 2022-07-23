package com.android.patmore.features.foryou.domain.usecases

import com.android.patmore.core.baseusecase.BaseUseCase
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForYouTweetsUseCase @Inject constructor(private val repository: IForYouRepository) :
    BaseUseCase<GetForYouTweetsUseCase.None, List<Pair<String, List<String>>>>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, List<Pair<String, List<String>>>>> {
        return repository.getForYouTweets()
    }
}
