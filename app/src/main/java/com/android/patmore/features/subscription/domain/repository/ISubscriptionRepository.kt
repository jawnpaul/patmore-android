package com.android.patmore.features.subscription.domain.repository

import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.subscription.data.remote.model.CreateSubscriptionRequest
import kotlinx.coroutines.flow.Flow

interface ISubscriptionRepository {
    suspend fun subscribeToTopic(request: CreateSubscriptionRequest): Flow<Either<Failure, String>>
}
