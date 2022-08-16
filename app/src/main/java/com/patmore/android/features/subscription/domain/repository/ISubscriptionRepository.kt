package com.patmore.android.features.subscription.domain.repository

import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionRequest
import kotlinx.coroutines.flow.Flow

interface ISubscriptionRepository {
    suspend fun subscribeToTopic(request: CreateSubscriptionRequest): Flow<Either<Failure, String>>
}
