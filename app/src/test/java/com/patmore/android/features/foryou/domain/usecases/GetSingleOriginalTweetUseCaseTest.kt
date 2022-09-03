package com.patmore.android.features.foryou.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.foryou.domain.model.ForYouTweet
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
class GetSingleOriginalTweetUseCaseTest : UnitTest() {

    private lateinit var getSingleOriginalTweetUseCase: GetSingleOriginalTweetUseCase

    @MockK
    private lateinit var iForYouRepository: IForYouRepository

    @Before
    fun setUp() {
        getSingleOriginalTweetUseCase = GetSingleOriginalTweetUseCase(iForYouRepository)
    }

    @Test
    fun `should call get original tweet`() = runTest {
        val params = "1233432,432121,12343"

        coEvery {
            iForYouRepository.getOriginalTweet(params)
        } returns flow {
            emit(
                Either.Right(
                    listOf(
                        ForYouTweet("1233432", "A tweet", "", null, null),
                        ForYouTweet("432121", "Another tweet", "", null, null),
                        ForYouTweet("12343", "A different tweet", "", null, null)
                    )
                )
            )
        }

        getSingleOriginalTweetUseCase.run(params)
        coVerify(exactly = 1) {
            iForYouRepository.getOriginalTweet(params)
        }
    }
}
