package com.patmore.android.features.authentication.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.authentication.domain.repository.IAuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAuthenticationTokenUseCaseTest : UnitTest() {

    private lateinit var getAuthenticationTokenUseCase: GetAuthenticationTokenUseCase

    @MockK
    private lateinit var iAuthenticationRepository: IAuthenticationRepository

    @Before
    fun setUp() {
        getAuthenticationTokenUseCase =
            GetAuthenticationTokenUseCase(iAuthenticationRepository)
    }

    @Test
    fun `should call get user token`() = runTest {
        val params = GetAuthenticationTokenUseCase.None()

        coEvery {
            iAuthenticationRepository.getUserToken()
        } returns flow {
            emit(Either.Right(Unit))
        }

        getAuthenticationTokenUseCase.run(params)
        coVerify(exactly = 1) {
            iAuthenticationRepository.getUserToken()
        }
    }
}
