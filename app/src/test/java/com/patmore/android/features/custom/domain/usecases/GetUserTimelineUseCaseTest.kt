package com.patmore.android.features.custom.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.custom.domain.model.TimelineTweet
import com.patmore.android.features.custom.domain.repository.ICustomRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserTimelineUseCaseTest : UnitTest() {

    private lateinit var getUserTimelineUseCase: GetUserTimelineUseCase

    @MockK
    private lateinit var iCustomRepository: ICustomRepository

    @Before
    fun setUp() {
        getUserTimelineUseCase = GetUserTimelineUseCase(iCustomRepository)
    }

    @Test
    fun `should call get user timeline`() = runTest {
        val params = GetUserTimelineUseCase.None()

        coEvery {
            iCustomRepository.getUserTimeline()
        } returns flow {
            emit(Either.Right(listOf(TimelineTweet("", "", "", null, null))))
        }

        getUserTimelineUseCase.run(params)
        coVerify(exactly = 1) {
            iCustomRepository.getUserTimeline()
        }
    }
}
