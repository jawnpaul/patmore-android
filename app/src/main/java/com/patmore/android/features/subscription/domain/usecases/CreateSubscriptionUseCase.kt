package com.patmore.android.features.subscription.domain.usecases

import com.patmore.android.core.baseusecase.BaseUseCase
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.patmore.android.features.subscription.domain.repository.ISubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateSubscriptionUseCase @Inject constructor(private val repository: ISubscriptionRepository) :
    BaseUseCase<CreateSubscriptionRequest, String>() {
    override suspend fun run(params: CreateSubscriptionRequest): Flow<Either<Failure, String>> {
        return repository.subscribeToTopic(params)
    }
}
