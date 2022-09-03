package com.patmore.android.features.foryou.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.foryou.domain.repository.IForYouRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetForYouTweetsUseCaseTest : UnitTest() {

    private lateinit var getForYouTweetsUseCase: GetForYouTweetsUseCase

    @MockK
    private lateinit var iForYouRepository: IForYouRepository

    @Before
    fun setUp() {
        getForYouTweetsUseCase = GetForYouTweetsUseCase(iForYouRepository)
    }

    @Test
    fun `should call get for you tweets`() = runTest {
        val params = GetForYouTweetsUseCase.None()

        coEvery {
            iForYouRepository.getForYouTweets()
        } returns flow {
            emit(Either.Right(listOf(Pair("Technology", listOf("tweet one", "tweet two")))))
        }

        getForYouTweetsUseCase.run(params)
        coVerify(exactly = 1) {
            iForYouRepository.getForYouTweets()
        }
    }
}
