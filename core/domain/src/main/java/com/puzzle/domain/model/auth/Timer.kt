package com.puzzle.domain.model.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Timer(
    private val durationInSec: Int = DEFAULT_DURATION_IN_SEC,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    fun startTimer(): Flow<Int> = flow {
        var remainingTime = durationInSec
        while (remainingTime > 0) {
            emit(remainingTime)
            delay(1000L)
            remainingTime--
        }
        emit(0) // 타이머 만료 시 0을 방출
    }.flowOn(dispatcher)

    private companion object {
        const val DEFAULT_DURATION_IN_SEC = 300
    }
}