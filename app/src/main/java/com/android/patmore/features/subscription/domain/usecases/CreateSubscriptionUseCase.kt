package com.android.patmore.features.subscription.domain.usecases

import com.android.patmore.core.baseusecase.BaseUseCase
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.android.patmore.features.subscription.domain.repository.ISubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateSubscriptionUseCase @Inject constructor(private val repository: ISubscriptionRepository) :
    BaseUseCase<CreateSubscriptionRequest, String>() {
    override suspend fun run(params: CreateSubscriptionRequest): Flow<Either<Failure, String>> {
        return repository.subscribeToTopic(params)
    }
}
