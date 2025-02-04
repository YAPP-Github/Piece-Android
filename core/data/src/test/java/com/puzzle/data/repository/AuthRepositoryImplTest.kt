package com.puzzle.data.repository

import com.puzzle.data.fake.source.auth.FakeAuthDataSource
import com.puzzle.data.fake.source.token.FakeLocalTokenDataSource
import com.puzzle.data.fake.source.user.FakeLocalUserDataSource
import com.puzzle.domain.model.auth.OAuthProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {
    private lateinit var authDataSource: FakeAuthDataSource
    private lateinit var tokenDataSource: FakeLocalTokenDataSource
    private lateinit var userInfoDataSource: FakeLocalUserDataSource
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setUp() {
        authDataSource = FakeAuthDataSource()
        tokenDataSource = FakeLocalTokenDataSource()
        userInfoDataSource = FakeLocalUserDataSource()
        authRepository = AuthRepositoryImpl(
            authDataSource = authDataSource,
            localTokenDataSource = tokenDataSource,
            localUserDataSource = userInfoDataSource,
        )
    }

    @Test
    fun `유저가 회원가입에 성공했을 경우 토큰과 유저 상태를 저장한다`() = runTest {
        // when
        val result = authRepository.loginOauth(OAuthProvider.KAKAO, "OAuthClientToken")

        // then
        assertTrue(tokenDataSource.accessToken.first().isNotEmpty())
        assertTrue(tokenDataSource.refreshToken.first().isNotEmpty())
        assertEquals("NONE", userInfoDataSource.userRole.first())
    }

    @Test
    fun `유저가 휴대폰 인증에 성공했을 경우 토큰과 유저 상태를 저장한다`() = runTest {
        // when
        val result = authRepository.verifyAuthCode("01012341234", "authCode")

        // then
        assertTrue(tokenDataSource.accessToken.first().isNotEmpty())
        assertTrue(tokenDataSource.refreshToken.first().isNotEmpty())
        assertEquals("REGISTER", userInfoDataSource.userRole.first())
    }

    @Test
    fun `로그아웃을 할 경우 로컬에 저장된 유저 데이터를 모두 제거한다`() = runTest {
        // given
        authRepository.loginOauth(OAuthProvider.KAKAO, "OAuthClientToken")

        // when
        val result = authRepository.logout()

        // then
        assertTrue(tokenDataSource.accessToken.first().isEmpty())
        assertTrue(tokenDataSource.refreshToken.first().isEmpty())
        assertTrue(userInfoDataSource.userRole.first().isEmpty())
    }
}
