package com.puzzle.domain

import com.puzzle.domain.repository.AuthCodeRepository
import com.puzzle.domain.usecase.RequestAuthCodeUseCase
import com.puzzle.domain.util.TimerManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RequestAuthCodeUseCaseTest {

    private lateinit var useCase: RequestAuthCodeUseCase
    private val mockAuthCodeRepository = mockk<AuthCodeRepository>()
    private val mockTimerManager = mockk<TimerManager>(relaxed = true)
    // relaxed = true 로 설정하면, 별도로 설정하지 않은 함수도 기본적으로 'empty' 동작

    // Callback도 MockK로 만든다.
    private val mockCallback = mockk<RequestAuthCodeUseCase.Callback>(relaxed = true)

    @BeforeEach
    fun setUp() {
        // 테스트마다 새로운 UseCase 생성
        useCase = RequestAuthCodeUseCase(
            authCodeRepository = mockAuthCodeRepository,
            timerManager = mockTimerManager
        )
    }

    @Test
    fun `repository가 false를 리턴하면, onRequestFail이 호출된다`() = runTest {
        // given
        coEvery { mockAuthCodeRepository.requestAuthCode(any()) } returns false

        // when
        useCase.invoke("01012345678", mockCallback)

        // then
        verify(exactly = 1) { mockCallback.onRequestFail(any()) }
        verify(exactly = 0) { mockCallback.onRequestSuccess() }
        verify(exactly = 0) { mockTimerManager.startTimer(any(), any(), any()) }
    }

    @Test
    fun `repository가 true를 리턴하면, onRequestSuccess와 타이머가 시작된다`() = runTest {
        // given
        coEvery { mockAuthCodeRepository.requestAuthCode(any()) } returns true

        // when
        useCase.invoke("01012345678", mockCallback)

        // then
        verify(exactly = 1) { mockCallback.onRequestSuccess() }
        verify(exactly = 1) {
            mockTimerManager.startTimer(
                durationInSec = 3,
                onTick = any(),
                onTimeExpired = any()
            )
        }
        verify(exactly = 0) { mockCallback.onRequestFail(any()) }
    }

    @Test
    fun `timerManager의 콜백(onTick, onTimeExpired)도 정상 호출되는지 확인`() = runTest {
        // given
        coEvery { mockAuthCodeRepository.requestAuthCode(any()) } returns true

        // CapturingSlot을 각각 생성
        val tickSlot = slot<(Int) -> Unit>()
        val expiredSlot = slot<() -> Unit>()

        // startTimer 호출 시 람다 캡처
        every {
            mockTimerManager.startTimer(
                durationInSec = any(),
                onTick = capture(tickSlot),
                onTimeExpired = capture(expiredSlot)
            )
        } just Runs

        // when
        useCase.invoke("01012345678", mockCallback)

        // then
        verify(exactly = 1) { mockTimerManager.startTimer(3, any(), any()) }
        verify(exactly = 1) { mockCallback.onRequestSuccess() }

        // 캡처된 람다 호출
        tickSlot.captured.invoke(2)
        verify(exactly = 1) { mockCallback.onTick(2) }

        expiredSlot.captured.invoke()
        verify(exactly = 1) { mockCallback.onTimeExpired() }
    }
}
