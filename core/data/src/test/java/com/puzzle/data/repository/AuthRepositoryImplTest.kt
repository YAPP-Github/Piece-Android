package com.puzzle.data.repository

import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.source.AuthDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {

    private lateinit var authDataSource: AuthDataSource
    private lateinit var tokenDataSource: LocalTokenDataSource
    private lateinit var userInfoDataSource: LocalUserDataSource
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setUp() {
        authDataSource = mockk()
        tokenDataSource = mockk()
        userInfoDataSource = mockk()
        authRepository = AuthRepositoryImpl(
            authDataSource = authDataSource,
            localTokenDataSource = tokenDataSource,
            localUserDataSource = userInfoDataSource,
        )

        coEvery { tokenDataSource.setAccessToken(any()) } just Runs
        coEvery { tokenDataSource.setRefreshToken(any()) } just Runs
        coEvery { userInfoDataSource.setUserRole(any()) } just Runs
        coEvery { userInfoDataSource.clearUserRole() } just Runs
        coEvery { tokenDataSource.clearToken() } just Runs
    }

    @Test
    fun `유저가 회원가입에 성공했을 경우 토큰과 유저 상태를 저장한다`() = runTest {
        // given
        val tokenResponse = LoginOauthResponse("NONE", "accessToken", "refreshToken")
        coEvery { authDataSource.loginOauth(any(), any()) } returns Result.success(tokenResponse)

        // when
        val result = authRepository.loginOauth(OAuthProvider.KAKAO, "OAuthClientToken")

        // then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken(any()) }
        coVerify { tokenDataSource.setRefreshToken(any()) }
        coVerify { userInfoDataSource.setUserRole("NONE") }
    }

    @Test
    fun `유저가 휴대폰 인증에 성공했을 경우 토큰과 유저 상태를 저장한다`() = runTest {
        // given
        val tokenResponse = VerifyAuthCodeResponse("REGISTER", "accessToken", "refreshToken")
        coEvery {
            authDataSource.verifyAuthCode(
                any(),
                any()
            )
        } returns Result.success(tokenResponse)

        // when
        val result = authRepository.verifyAuthCode("01012341234", "authCode")

        // then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken(any()) }
        coVerify { tokenDataSource.setRefreshToken(any()) }
        coVerify { userInfoDataSource.setUserRole("REGISTER") }
    }

    @Test
    fun `로그아웃을 할 경우 로컬에 저장된 유저 데이터를 모두 제거한다`() = runTest {
        // when
        val result = authRepository.logout()

        // then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.clearToken() }
        coVerify { userInfoDataSource.clearUserRole() }
    }
}
