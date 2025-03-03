package com.puzzle.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime

class TimeUtilTest {

    @Test
    fun `올바른 형식의 문자열을 DateTime으로 변환할 수 있다`() {
        // given
        val dateTimeString = "2024-06-01T00:00:00"
        val expected = LocalDateTime.parse(dateTimeString)

        // when
        val actual = dateTimeString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `null 값을 DateTime으로 파싱하려고 할 경우 LocalDateTime_MIN을 반환한다`() {
        // given
        val nullString: String? = null
        val expected = LocalDateTime.MIN

        // when
        val actual = nullString.parseDateTime()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `형식에 맞지 않은 문자열을 DateTime으로 파싱하려고 할 경우 LocalDateTime_MIN을 반환한다`() {
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

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2023051",    // 7자리 (너무 짧음)
            "202305155",  // 9자리 (너무 김)
            "abcdefgh",   // 숫자가 아님
            "2023/05/15", // 특수문자 포함
            "",           // 빈 문자열
            "2023 0515",  // 공백 포함
            " 20230515 "  // 앞뒤 공백 포함
        ]
    )
    fun `잘못된 형식을 생일 날짜로 변환하려고 할 때 IllegalArgumentException을 발생시킨다`(input: String) {
        assertThrows<IllegalArgumentException> {
            input.toBirthDate()
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2023051",    // 7자리 (너무 짧음)
            "202305155",  // 9자리 (너무 김)
            "abcdefgh",   // 숫자가 아님
            "2023/05/15", // 특수문자 포함
            "",           // 빈 문자열
            "2023 0515",  // 공백 포함
            " 20230515 "  // 앞뒤 공백 포함
        ]
    )
    fun `잘못된 형식의 생일 날짜는 false를 반환한다`(input: String) {
        assertFalse(input.isValidBirthDateFormat())
    }
}