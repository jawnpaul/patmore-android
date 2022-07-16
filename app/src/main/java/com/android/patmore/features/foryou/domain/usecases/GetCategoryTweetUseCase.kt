package com.android.patmore.features.foryou.domain.usecases

import com.android.patmore.core.baseusecase.BaseUseCase
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.foryou.domain.model.ForYouTweet
import com.android.patmore.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryTweetUseCase @Inject constructor(private val repository: IForYouRepository) :
    BaseUseCase<String, List<ForYouTweet>>() {
    override suspend fun run(params: String): Flow<Either<Failure, List<ForYouTweet>>> {
        return repository.getCategoryTweets(params)
    }
}
