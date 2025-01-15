package com.puzzle.domain.model.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Timer @Inject constructor() {
    fun startTimer(durationInSec: Int = DEFAULT_DURATION_IN_SEC): Flow<Int> = flow {
        var remainingTime = durationInSec

        while (remainingTime > 0) {
            emit(remainingTime)
            delay(TIMER_TICK)
            remainingTime--
        }

        emit(END_TIMER_FLAG) // 타이머 만료를 나타내는 플래그를 방출
    }

    companion object {
        const val DEFAULT_DURATION_IN_SEC = 300
        const val END_TIMER_FLAG = -1
        private const val TIMER_TICK = 1000L
    }
}