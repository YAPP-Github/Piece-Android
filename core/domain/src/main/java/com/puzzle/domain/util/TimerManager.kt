package com.puzzle.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

object TimerManager {
    private var timerJob: Job? = null
    private val remainingTime = AtomicInteger(0)

    fun startTimer(
        durationInSec: Int,
        onTick: (remainingSec: Int) -> Unit,
        onTimeExpired: () -> Unit
    ) {
        stopTimer()

        remainingTime.set(durationInSec)

        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (remainingTime.get() >= 0) {
                withContext(Dispatchers.Main) {
                    onTick(remainingTime.get())
                }

                if (remainingTime.get() == 0) {
                    break
                }

                delay(1000L)
                remainingTime.decrementAndGet()
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
    }

    fun isTimeRemaining(): Boolean {
        return remainingTime.get() > 0
    }
}
