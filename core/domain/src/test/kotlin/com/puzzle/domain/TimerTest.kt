package com.puzzle.domain

import com.puzzle.domain.model.auth.Timer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TimerTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var timer: Timer

    @BeforeEach
    fun setUp() {
        timer = Timer(durationInSec = 5, dispatcher = testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun `타이머가 지속 시간으로 올바르게 카운트다운을 방출하는지 검증`() = testScope.runTest {
        // 주어진 상황
        val expected = listOf(5, 4, 3, 2, 1, 0)

        val flow = timer.startTimer()

        val results = flow.toList()

        // 검증 단계
        Assertions.assertEquals(expected, results)
    }

    @Test
    fun `타이머가 완료되고 0을 올바르게 방출하는지 검증`() = testScope.runTest {
        // 주어진 상황
        val customDuration = 1
        val expected = listOf(1, 0)

        val flow = Timer(durationInSec = customDuration, dispatcher = testDispatcher).startTimer()

        val results = flow.toList()

        Assertions.assertEquals(expected, results)
    }
}