package com.patmore.android.features.subscription.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.patmore.android.features.subscription.domain.repository.ISubscriptionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateSubscriptionUseCaseTest : UnitTest() {

    private lateinit var createSubscriptionUseCase: CreateSubscriptionUseCase

    @MockK
    private lateinit var iSubscriptionRepository: ISubscriptionRepository

    @Before
    fun setUp() {
        createSubscriptionUseCase = CreateSubscriptionUseCase(iSubscriptionRepository)
    }

    @Test
    fun `should call subscribe to topic`() = runTest {
        val params = CreateSubscriptionRequest(listOf("technology"))
        coEvery {
            iSubscriptionRepository.subscribeToTopic(params)
        } returns flow {
            emit(Either.Right("Success"))
        }
        createSubscriptionUseCase.run(params)
        coVerify(exactly = 1) {
            iSubscriptionRepository.subscribeToTopic(params)
        }
    }
}
