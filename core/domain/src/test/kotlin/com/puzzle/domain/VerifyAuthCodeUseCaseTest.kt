package com.puzzle.domain

import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.usecase.VerifyAuthCodeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VerifyAuthCodeUseCaseTest {
    private lateinit var useCase: VerifyAuthCodeUseCase
    private lateinit var authRepository: AuthRepository
    private lateinit var timer: Timer
    private lateinit var callback: VerifyAuthCodeUseCase.Callback

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        timer = mockk(relaxed = true)
        callback = mockk(relaxed = true)

        useCase = VerifyAuthCodeUseCase(authRepository)
    }

    @Test
    fun `남은 시간이 없으면 아무 동작도 하지 않는다`() = runTest {
        every { timer.isTimeRemaining() } returns false

        useCase.invoke("1234", timer, callback)

        coVerify(exactly = 0) { authRepository.verifyAuthCode(any()) }
        verify(exactly = 0) { timer.pauseTimer() }
        verify(exactly = 0) { timer.stopTimer() }
        verify(exactly = 0) { timer.resumeTimer() }
        verify(exactly = 0) { callback.onVerificationCompleted() }
        verify(exactly = 0) { callback.onVerificationFailed(any()) }
    }

    @Test
    fun `인증 성공 시 - pauseTimer 후 stopTimer, onVerificationCompleted가 호출된다`() = runTest {
        every { timer.isTimeRemaining() } returns true
        coEvery { authRepository.verifyAuthCode("1234") } returns true

        useCase.invoke("1234", timer, callback)

        coVerifyOrder {
            timer.isTimeRemaining()
            timer.pauseTimer()
            authRepository.verifyAuthCode("1234")
            timer.stopTimer()
            callback.onVerificationCompleted()
        }
        verify(exactly = 0) { timer.resumeTimer() }
        verify(exactly = 0) { callback.onVerificationFailed(any()) }
    }

    @Test
    fun `인증 실패 시 - pauseTimer 후 resumeTimer, onVerificationFailed가 호출된다`() = runTest {
        every { timer.isTimeRemaining() } returns true
        coEvery { authRepository.verifyAuthCode("9999") } returns false

        useCase.invoke("9999", timer, callback)

        coVerifyOrder {
            timer.isTimeRemaining()
            timer.pauseTimer()
            authRepository.verifyAuthCode("9999")
            timer.resumeTimer()
            callback.onVerificationFailed(any())
        }
        verify(exactly = 0) { timer.stopTimer() }
        verify(exactly = 0) { callback.onVerificationCompleted() }
    }

    @Test
    fun `인증 성공 네트워크 지연 테스트`() = runTest {
        // given
        every { timer.isTimeRemaining() } returns true

        // 네트워크 지연 1초
        coEvery { authRepository.verifyAuthCode("1234") } coAnswers {
            delay(1000L)
            true
        }

        // when
        useCase.invoke("1234", timer, callback)

        // then
        // pauseTimer()가 먼저 불렸는지 확인
        verify { timer.pauseTimer() }

        // 가상 시간 1초를 진행 -> 이 시간 동안 timer는 Pause 상태
        advanceTimeBy(1000L)
        runCurrent()

        // verifyAuthCode()가 완료되면 인증 성공 시나리오 → stopTimer(), onVerificationCompleted()
        coVerifyOrder {
            authRepository.verifyAuthCode("1234")
            timer.stopTimer()
            callback.onVerificationCompleted()
        }

        // 네트워크 대기 동안 resumeTimer()는 호출되지 않아야 함
        verify(exactly = 0) { timer.resumeTimer() }
        verify(exactly = 0) { callback.onVerificationFailed(any()) }
    }
}