package com.puzzle.domain.model.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class Timer(
    private val timerScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {
    private var timerJob: Job? = null

    private val remainingTime = AtomicInteger(0)

    private val isPaused = AtomicBoolean(false)

    fun startTimer(
        onTick: (remainingSec: Int) -> Unit,
        onTimeExpired: () -> Unit,
        durationInSec: Int = DURATION_IN_SEC
    ) {
        stopTimer()

        remainingTime.set(durationInSec)
        isPaused.set(false)

        timerJob = timerScope.launch {
            while (remainingTime.get() > 0) {
                if (!isPaused.get()) {
                    withContext(Dispatchers.Main) {
                        onTick(remainingTime.get())
                    }
                    delay(1000L)
                    remainingTime.decrementAndGet()
                } else {
                    // 일시정지 상태일 때는 0.2초 정도 대기
                    delay(200L)
                }
            }

            withContext(Dispatchers.Main) {
                onTimeExpired()
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        remainingTime.set(0)
        isPaused.set(false)
    }

    fun pauseTimer() {
        isPaused.set(true)
    }

    fun resumeTimer() {
        if (remainingTime.get() > 0 && isPaused.get()) {
            isPaused.set(false)
        }
    }

    fun isTimeRemaining(): Boolean {
        return remainingTime.get() > 0
    }

    private companion object {
        const val DURATION_IN_SEC = 3
    }
}