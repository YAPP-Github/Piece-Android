package com.puzzle.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * String?을 LocalDateTime으로 변환합니다.
 *
 * - 문자열이 null인 경우, [LocalDateTime.MIN]을 반환합니다.
 * - 잘못된 형식으로 인해 파싱에 실패할 경우, [LocalDateTime.MIN]을 반환합니다.
 */
fun String?.parseDateTime(): LocalDateTime {
    return try {
        this?.let { LocalDateTime.parse(it) } ?: LocalDateTime.MIN
    } catch (e: DateTimeParseException) {
        LocalDateTime.MIN
    }
}

fun String.toBirthDate(): String {
    require(matches("^\\d{8}$".toRegex())) { "잘못된 날짜 형식입니다. YYYYMMDD 형식이어야 하는데, 입력된 값은 $this 입니다." }

    return replace(
        "^(\\d{4})(\\d{2})(\\d{2})$".toRegex(),
        "$1-$2-$3"
    )
}

/**
 * LocalDateTime을 "YYYY년 MM월 DD일 HH:mm" 형식의 문자열로 변환합니다.
 */
fun LocalDateTime.toBlockSyncFormattedTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")
    return this.format(formatter)
}
