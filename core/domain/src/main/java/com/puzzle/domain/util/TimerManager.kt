package com.puzzle.domain.util

interface TimerManager {
    fun startTimer(
        durationInSec: Int,
        onTick: (remainingSec: Int) -> Unit,
        onTimeExpired: () -> Unit
    )

    fun stopTimer()
    fun isTimeRemaining(): Boolean
}
