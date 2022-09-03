package com.patmore.android.features.custom.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
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
class GetCurrentUserUseCaseTest : UnitTest() {

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @MockK
    private lateinit var iCustomRepository: ICustomRepository

    @Before
    fun setUp() {
        getCurrentUserUseCase = GetCurrentUserUseCase(iCustomRepository)
    }

    @Test
    fun `should call get current user`() = runTest {
        val params = GetCurrentUserUseCase.None()

        coEvery {
            iCustomRepository.getCurrentUser()
        } returns flow {
            emit(Either.Right(Unit))
        }

        getCurrentUserUseCase.run(params)
        coVerify(exactly = 1) {
            iCustomRepository.getCurrentUser()
        }
    }
}
