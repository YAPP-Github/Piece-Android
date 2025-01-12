package com.puzzle.domain

import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.usecase.RequestAuthCodeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RequestAuthCodeUseCaseTest {
    private lateinit var useCase: RequestAuthCodeUseCase
    private lateinit var authRepository: AuthRepository
    private lateinit var timer: Timer
    private lateinit var callback: RequestAuthCodeUseCase.Callback

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        timer = mockk(relaxed = true)
        callback = mockk(relaxed = true)

        useCase = RequestAuthCodeUseCase(authRepository)
    }

    @Test
    fun `requestAuthCode가 false면 onRequestFail 호출, 타이머 시작 안 함`() = runTest {
        // given
        coEvery { authRepository.requestAuthCode("010-1234-5678") } returns false

        // when
        useCase.invoke("010-1234-5678", timer, callback)

        // then
        coVerify { authRepository.requestAuthCode("010-1234-5678") }
        verify(exactly = 1) { callback.onRequestFail(any()) }
        verify(exactly = 0) { timer.startTimer(any(), any(), any()) }
        verify(exactly = 0) { callback.onRequestSuccess() }
    }

    @Test
    fun `requestAuthCode가 true면 onRequestSuccess 호출 후 타이머 시작`() = runTest {
        // given
        coEvery { authRepository.requestAuthCode("010-0000-0000") } returns true

        // when
        useCase.invoke("010-0000-0000", timer, callback)

        // then
        coVerify { authRepository.requestAuthCode("010-0000-0000") }
        verify { callback.onRequestSuccess() }
        verify { timer.startTimer(any(), any(), any()) }
        verify(exactly = 0) { callback.onRequestFail(any()) }
    }
}
