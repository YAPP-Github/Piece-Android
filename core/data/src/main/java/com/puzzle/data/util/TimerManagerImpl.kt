package com.puzzle.data.util

import com.puzzle.domain.util.TimerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class TimerManagerImpl @Inject constructor() : TimerManager {
    private var timerJob: Job? = null
    private val remainingTime = AtomicInteger(0)

    override fun startTimer(
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

    override fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        remainingTime.set(0)
    }

    override fun isTimeRemaining(): Boolean {
        return remainingTime.get() > 0
    }
}
