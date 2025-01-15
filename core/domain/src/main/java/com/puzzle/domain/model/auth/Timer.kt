package com.puzzle.domain.model.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Timer(
    private val durationInSec: Int = DEFAULT_DURATION_IN_SEC,
) {
    fun startTimer(): Flow<Int> = flow {
        var remainingTime = durationInSec

        while (remainingTime > 0) {
            emit(remainingTime)
            delay(TIMER_TICK)
            remainingTime--
        }

        emit(END_TIMER_FLAG) // 타이머 만료를 나타내는 플래그를 방출
    }

    companion object {
        private const val DEFAULT_DURATION_IN_SEC = 300
        private const val TIMER_TICK = 1000L
        const val END_TIMER_FLAG = -1
    }
}