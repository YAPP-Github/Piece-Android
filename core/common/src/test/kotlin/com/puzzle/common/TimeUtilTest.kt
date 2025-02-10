package com.puzzle.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class TimeUtilTest {

    @Test
    fun `올바른 형식의 문자열을 LocalDateTime으로 변환할 수 있다`() {
        // given
        val dateTimeString = "2024-06-01T00:00:00"
        val expected = LocalDateTime.parse(dateTimeString)

        // when
        val actual = dateTimeString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `null 값을 파싱하려고 할 경우 LocalDateTime_MIN을 반환한다`() {
        // given
        val nullString: String? = null
        val expected = LocalDateTime.MIN

        // when
        val actual = nullString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `형식에 맞지 않은 문자열을 파싱하려고 할 경우 LocalDateTime_MIN을 반환한다`() {
        // given
        val invalidString = "invalid-date-format"
        val expected = LocalDateTime.MIN

        // when
        val actual = invalidString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `유효한 YYYYMMDD 문자열을 YYYY-MM-DD 형식으로 변환한다`() {
        assertEquals("2023-05-15", "20230515".toBirthDate())
        assertEquals("1990-01-01", "19900101".toBirthDate())
        assertEquals("2025-12-31", "20251231".toBirthDate())
    }

    @Test
    fun `잘못된 형식에 대해 IllegalArgumentException을 발생시킨다`() {
        assertThrows<IllegalArgumentException> { "2023051".toBirthDate() }
        assertThrows<IllegalArgumentException> { "202305155".toBirthDate() }
        assertThrows<IllegalArgumentException> { "abcdefgh".toBirthDate() }
        assertThrows<IllegalArgumentException> { "2023/05/15".toBirthDate() }
    }

    @Test
    fun `빈 문자열에 대해 IllegalArgumentException을 발생시킨다`() {
        assertThrows<IllegalArgumentException> { "".toBirthDate() }
    }

    @Test
    fun `공백이 포함된 문자열에 대해 IllegalArgumentException을 발생시킨다`() {
        assertThrows<IllegalArgumentException> { "2023 0515".toBirthDate() }
        assertThrows<IllegalArgumentException> { " 20230515 ".toBirthDate() }
    }
}
