package com.patmore.android.features.authentication.domain.usecases

import com.patmore.android.UnitTest
import com.patmore.android.core.functional.Either
import com.patmore.android.features.authentication.data.remote.model.AccessTokenRequest
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
class GetTwitterUserAccessTokenUseCaseTest : UnitTest() {

    private lateinit var getTwitterUserAccessTokenUseCase: GetTwitterUserAccessTokenUseCase

    @MockK
    private lateinit var iAuthenticationRepository: IAuthenticationRepository

    @Before
    fun setUp() {
        getTwitterUserAccessTokenUseCase =
            GetTwitterUserAccessTokenUseCase(iAuthenticationRepository)
    }

    @Test
    fun `should call get OAuth2 access token`() = runTest {
        val params = AccessTokenRequest(code = "", challenge = "", clientID = "", callback = "")

        coEvery {
            iAuthenticationRepository.getOauth2AccessToken(params)
        } returns flow {
            emit(Either.Right(Unit))
        }

        getTwitterUserAccessTokenUseCase.run(params)
        coVerify(exactly = 1) {
            iAuthenticationRepository.getOauth2AccessToken(params)
        }
    }
}
