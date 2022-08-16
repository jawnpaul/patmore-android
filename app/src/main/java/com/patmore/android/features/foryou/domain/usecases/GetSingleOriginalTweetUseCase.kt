package com.patmore.android.features.foryou.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.foryou.domain.model.ForYouTweet
import com.patmore.android.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSingleOriginalTweetUseCase @Inject constructor(private val repository: IForYouRepository) :
    BaseUseCase<String, List<ForYouTweet>>() {
    override suspend fun run(params: String): Flow<Either<Failure, List<ForYouTweet>>> {
        return repository.getOriginalTweet(params)
    }
}
