package com.patmore.android.features.foryou.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForYouTweetsUseCase @Inject constructor(private val repository: IForYouRepository) :
    BaseUseCase<GetForYouTweetsUseCase.None, List<Pair<String, List<String>>>>() {
    class None

    override suspend fun run(params: None): Flow<Either<Failure, List<Pair<String, List<String>>>>> {
        return repository.getForYouTweets()
    }
}
