package com.puzzle.domain

import com.puzzle.domain.repository.AuthCodeRepository
import com.puzzle.domain.usecase.VerifyAuthCodeUseCase
import com.puzzle.domain.util.TimerManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class VerifyAuthCodeUseCaseTest {

    private lateinit var useCase: VerifyAuthCodeUseCase
    private val mockAuthCodeRepository = mockk<AuthCodeRepository>()
    private val mockTimerManager = mockk<TimerManager>(relaxed = true)
    private val mockCallback = mockk<VerifyAuthCodeUseCase.Callback>(relaxed = true)

    @BeforeEach
    fun setUp() {
        useCase = VerifyAuthCodeUseCase(
            authCodeRepository = mockAuthCodeRepository,
            timerManager = mockTimerManager
        )
    }

    @Test
    fun `타이머에 시간이 남아있지 않다면, 아무 동작도 하지 않는다`() = runTest {
        // given
        every { mockTimerManager.isTimeRemaining() } returns false

        // when
        useCase.invoke("123456", mockCallback)

        // then
        verify(exactly = 1) { mockTimerManager.isTimeRemaining() }
        coVerify(exactly = 0) { mockAuthCodeRepository.verify(any()) }
        verify(exactly = 0) { mockCallback.onVerificationCompleted() }
        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
    }

    @Test
    fun `타이머 시간이 남아있다면, verify 성공 시 타이머 중지 후 onVerificationCompleted 호출`() = runTest {
        // given
        every { mockTimerManager.isTimeRemaining() } returns true
        coEvery { mockAuthCodeRepository.verify("123456") } returns true

        // when
        useCase.invoke("123456", mockCallback)

        // then
        verify(exactly = 1) { mockTimerManager.isTimeRemaining() }
        coVerify(exactly = 1) { mockAuthCodeRepository.verify("123456") }
        verify(exactly = 1) { mockTimerManager.stopTimer() }
        verify(exactly = 1) { mockCallback.onVerificationCompleted() }
        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
    }

    @Test
    fun `타이머 시간이 남아있다면, verify 실패 시 onVerificationFailed 호출`() = runTest {
        // given
        every { mockTimerManager.isTimeRemaining() } returns true
        coEvery { mockAuthCodeRepository.verify("000000") } returns false

        // when
        useCase.invoke("000000", mockCallback)

        // then
        verify(exactly = 1) { mockTimerManager.isTimeRemaining() }
        coVerify(exactly = 1) { mockAuthCodeRepository.verify("000000") }
        verify(exactly = 0) { mockTimerManager.stopTimer() }
        verify(exactly = 1) { mockCallback.onVerificationFailed(any()) }
        verify(exactly = 0) { mockCallback.onVerificationCompleted() }
    }

//    @Test
//    fun `네트워크 지연 후 verify 성공 시 onVerificationCompleted 호출`() = runTest {
//        // given
//        every { mockTimerManager.isTimeRemaining() } returns true
//        coEvery { mockAuthCodeRepository.verify("123456") } coAnswers {
//            delay(1000L) // 네트워크 지연 시뮬레이션
//            true
//        }
//
//        // when
//        val job = launch {
//            useCase.invoke("123456", mockCallback)
//        }
//
//        // verify isTimeRemaining 호출
//        verify(exactly = 1) { mockTimerManager.isTimeRemaining() }
//
//        // verify verify 호출이 아직 완료되지 않았음을 확인
//        coVerify(exactly = 0) { mockAuthCodeRepository.verify("123456") }
//
//        // 가상 시간 500ms 진행
//        advanceTimeBy(500L)
//
//        // verify 아직 콜백이 호출되지 않았음을 확인
//        verify(exactly = 0) { mockCallback.onVerificationCompleted() }
//        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
//
//        // 가상 시간 500ms 더 진행 (총 1000ms)
//        advanceTimeBy(500L)
//
//        // verify verify 호출이 완료되고 콜백이 호출되었는지 확인
//        coVerify(exactly = 1) { mockAuthCodeRepository.verify("123456") }
//        verify(exactly = 1) { mockTimerManager.stopTimer() }
//        verify(exactly = 1) { mockCallback.onVerificationCompleted() }
//        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
//
//        job.join()
//    }
//
//    @Test
//    fun `네트워크 지연 후 verify 실패 시 onVerificationFailed 호출`() = runTest {
//        // given
//        every { mockTimerManager.isTimeRemaining() } returns true
//        coEvery { mockAuthCodeRepository.verify("000000") } coAnswers {
//            delay(1000L) // 네트워크 지연 시뮬레이션
//            false
//        }
//
//        // when
//        val job = launch {
//            useCase.invoke("000000", mockCallback)
//        }
//
//        // verify isTimeRemaining 호출
//        verify(exactly = 1) { mockTimerManager.isTimeRemaining() }
//
//        // verify verify 호출이 아직 완료되지 않았음을 확인
//        coVerify(exactly = 0) { mockAuthCodeRepository.verify("000000") }
//
//        // 가상 시간 500ms 진행
//        advanceTimeBy(500L)
//
//        // verify 아직 콜백이 호출되지 않았음을 확인
//        verify(exactly = 0) { mockCallback.onVerificationCompleted() }
//        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
//
//        // 가상 시간 500ms 더 진행 (총 1000ms)
//        advanceTimeBy(500L)
//
//        // verify verify 호출이 완료되고 콜백이 호출되었는지 확인
//        coVerify(exactly = 1) { mockAuthCodeRepository.verify("000000") }
//        verify(exactly = 0) { mockTimerManager.stopTimer() }
//        verify(exactly = 1) { mockCallback.onVerificationFailed(any()) }
//        verify(exactly = 0) { mockCallback.onVerificationCompleted() }
//
//        job.join()
//    }
//
//    @Test
//    fun `네트워크 지연 중 타이머 만료 시 onVerificationCompleted 호출`() = runTest {
//        // given
//        every { mockTimerManager.isTimeRemaining() } returns true
//        coEvery { mockAuthCodeRepository.verify("123456") } coAnswers {
//            delay(1000L) // 네트워크 지연 시뮬레이션
//            true
//        }
//
//        // when
//        val job = launch {
//            useCase.invoke("123456", mockCallback)
//        }
//
//        // verify isTimeRemaining 호출
//        verify(exactly = 1) { mockTimerManager.isTimeRemaining() }
//
//        // verify verify 호출이 아직 완료되지 않았음을 확인
//        coVerify(exactly = 0) { mockAuthCodeRepository.verify("123456") }
//
//        // 가상 시간 1000ms 진행 (verify 호출 완료)
//        advanceTimeBy(1000L)
//
//        // verify verify 호출이 완료되고 콜백이 호출되었는지 확인
//        coVerify(exactly = 1) { mockAuthCodeRepository.verify("123456") }
//        verify(exactly = 1) { mockTimerManager.stopTimer() }
//        verify(exactly = 1) { mockCallback.onVerificationCompleted() }
//        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
//
//        job.join()
//    }
//
//    @Test
//    fun `네트워크 지연 중 타이머 만료되더라도 verify 결과에 따라 콜백 호출`() = runTest {
//        // given
//        every { mockTimerManager.isTimeRemaining() } returns true
//        coEvery { mockAuthCodeRepository.verify("123456") } coAnswers {
//            delay(2000L) // 네트워크 지연 시뮬레이션
//            true
//        }
//
//        // when
//        val job = launch {
//            useCase.invoke("123456", mockCallback)
//        }
//
//        // 가상 시간 1000ms 진행 (verify 호출 중)
//        advanceTimeBy(1000L)
//
//        // 타이머 만료를 시뮬레이션 (timerManager.stopTimer()가 호출되지 않으므로)
//        // 현재 VerifyAuthCodeUseCase는 타이머 상태를 재확인하지 않으므로, 추가적인 콜백 호출이 없습니다.
//
//        // verify verify 호출이 아직 완료되지 않았음을 확인
//        coVerify(exactly = 0) { mockAuthCodeRepository.verify("123456") }
//
//        // 가상 시간 1000ms 더 진행 (총 2000ms, verify 호출 완료)
//        advanceTimeBy(1000L)
//
//        // verify verify 호출이 완료되고 콜백이 호출되었는지 확인
//        coVerify(exactly = 1) { mockAuthCodeRepository.verify("123456") }
//        verify(exactly = 1) { mockTimerManager.stopTimer() }
//        verify(exactly = 1) { mockCallback.onVerificationCompleted() }
//        verify(exactly = 0) { mockCallback.onVerificationFailed(any()) }
//
//        job.join()
//    }
}
