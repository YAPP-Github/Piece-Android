package com.puzzle.domain

import com.puzzle.domain.model.auth.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(ExperimentalCoroutinesApi::class)
class TimerTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var timer: Timer

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startTimer - 지정된 시간동안 tick 후 만료`() = runTest {
        val tickList = mutableListOf<Int>()
        val expiredFlag = AtomicBoolean(false)

        val testScope = CoroutineScope(SupervisorJob() + testDispatcher)
        timer = Timer(testScope)

        // 타이머 시작 (3초)
        timer.startTimer(
            onTick = { sec -> tickList.add(sec) },
            onTimeExpired = { expiredFlag.set(true) },
            durationInSec = 3
        )

        // 아직 아무것도 진행 안 됨 → runCurrent()로 초기 코루틴 실행
        runCurrent()

        // 1초 진행
        advanceTimeBy(1000)
        runCurrent()
        assertEquals(listOf(3, 2), tickList, "1초 후 tick=3")

        // 또 1초
        advanceTimeBy(1000)
        runCurrent()
        assertEquals(listOf(3, 2, 1), tickList, "2초 후 tick=2")

        // 마지막 1초
        advanceTimeBy(1000)
        runCurrent()
        assertEquals(listOf(3, 2, 1), tickList, "3초 후 tick=1")

        // 만료 콜백 호출됐는지
        assertTrue(expiredFlag.get(), "onTimeExpired가 호출되어야 함")
        // 시간 없다고 표시
        assertFalse(timer.isTimeRemaining())
    }

    @Test
    fun `pauseTimer - 일시정지 중에는 tick이 증가하지 않는다`() = runTest {
        val tickList = mutableListOf<Int>()
        val testScope = CoroutineScope(SupervisorJob() + testDispatcher)
        timer = Timer(testScope)

        timer.startTimer(
            onTick = { tickList.add(it) },
            onTimeExpired = {},
            durationInSec = 5
        )

        // 1초 진행 → tick 여러 번 (ex: [5,4])
        advanceTimeBy(1000)
        runCurrent()

        // pause
        timer.pauseTimer()
        // 2초 진행
        advanceTimeBy(2000)
        runCurrent()

        // tickList 그대로인지 확인
        assertEquals(listOf(5, 4), tickList)

        testScope.cancel()
    }

    @Test
    fun `resumeTimer - 일시정지 해제 후 다시 tick이 진행된다`() = runTest {
        val tickList = mutableListOf<Int>()
        val testScope = CoroutineScope(SupervisorJob() + testDispatcher)
        timer = Timer(testScope)

        timer.startTimer(
            onTick = { tickList.add(it) },
            onTimeExpired = {},
            durationInSec = 3
        )
        runCurrent()

        // 1초 진행
        advanceTimeBy(1000)
        runCurrent()
        assertEquals(listOf(3, 2), tickList)

        // pause
        timer.pauseTimer()

        // 1초 더 → tick 증가 없음
        advanceTimeBy(1000)
        runCurrent()
        assertEquals(listOf(3, 2), tickList)

        // resume
        timer.resumeTimer()

        // 2초 더 진행 → 나머지 시간(2초) 소진
        advanceTimeBy(2000)
        runCurrent()

        // tick이 2번 더 발생 → [3, 2, 1]
        assertEquals(listOf(3, 2, 1), tickList)
        assertFalse(timer.isTimeRemaining())
    }

    @Test
    fun `stopTimer - 즉시 종료되고, onTimeExpired는 호출되지 않는다`() = runTest {
        val tickList = mutableListOf<Int>()
        val expiredFlag = AtomicBoolean(false)
        val testScope = CoroutineScope(SupervisorJob() + testDispatcher)
        timer = Timer(testScope)

        timer.startTimer(
            onTick = { tickList.add(it) },
            onTimeExpired = { expiredFlag.set(true) },
            durationInSec = 3
        )
        runCurrent()

        advanceTimeBy(1000)
        runCurrent()
        assertEquals(listOf(3, 2), tickList)

        // stopTimer
        timer.stopTimer()
        advanceTimeBy(5000)
        runCurrent()

        // tick이 더 이상 증가 X
        assertEquals(listOf(3, 2), tickList)
        // onTimeExpired도 호출되지 않음
        assertFalse(expiredFlag.get())
        // 시간은 없는 것으로 표시
        assertFalse(timer.isTimeRemaining())
    }
}