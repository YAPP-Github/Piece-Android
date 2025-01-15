package com.puzzle.domain

import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.model.auth.Timer.Companion.END_TIMER_FLAG
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TimerTest {

    private lateinit var timer: Timer

    @BeforeEach
    fun setUp() {
        timer = Timer(durationInSec = 5)
    }

    @Test
    fun `타이머는 1초마다 남은 시간을 방출한다`() = runTest {
        // given
        val expected = listOf(5, 4, 3, 2, 1, END_TIMER_FLAG)

        // when
        val flow = timer.startTimer()

        // then
        val actual = flow.toList()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `시간이 완료되면 타이머가 완료되었음을 나타내는 플래그를 방출한다`() = runTest {
        // given
        val expected = listOf(1, END_TIMER_FLAG)

        // when
        val flow = Timer(1).startTimer()

        // then
        val actual = flow.toList()
        Assertions.assertEquals(expected, actual)
    }
}