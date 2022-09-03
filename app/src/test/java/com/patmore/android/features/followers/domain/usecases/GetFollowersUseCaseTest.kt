package com.patmore.android.features.followers.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.followers.domain.repository.IFollowersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFollowersUseCaseTest : UnitTest() {

    private lateinit var getFollowersUseCase: GetFollowersUseCase

    @MockK
    private lateinit var iFollowersRepository: IFollowersRepository

    @Before
    fun setUp() {
        getFollowersUseCase = GetFollowersUseCase(iFollowersRepository)
    }

    @Test
    fun `should call get user followers`() = runTest {
        val params = GetFollowersUseCase.None()

        coEvery {
            iFollowersRepository.getUserFollowers()
        } returns flow {
            emit(Either.Right(Unit))
        }

        getFollowersUseCase.run(params)
        coVerify(exactly = 1) {
            iFollowersRepository.getUserFollowers()
        }
    }
}
