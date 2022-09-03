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
class GetCategoryTweetUseCaseTest : UnitTest() {

    private lateinit var getCategoryTweetUseCase: GetCategoryTweetUseCase

    @MockK
    private lateinit var iForYouRepository: IForYouRepository

    @Before
    fun setUp() {
        getCategoryTweetUseCase = GetCategoryTweetUseCase(iForYouRepository)
    }

    @Test
    fun `should call get category tweets`() = runTest {
        val params = "technology"

        coEvery {
            iForYouRepository.getCategoryTweets(params)
        } returns flow {
            emit(Either.Right(listOf("tweet one", "tweet two")))
        }

        getCategoryTweetUseCase.run(params)
        coVerify(exactly = 1) {
            iForYouRepository.getCategoryTweets(params)
        }
    }
}
