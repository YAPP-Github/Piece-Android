package com.puzzle.domain.model.match

import com.puzzle.domain.model.match.MatchInfo.Companion.SECOND_IN_MILLIS
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class MatchInfo(
    val matchId: Int,
    val matchStatus: MatchStatus,
    val description: String,
    val nickname: String,
    val birthYear: String,
    val location: String,
    val job: String,
    val matchedValueCount: Int,
    val matchedValueList: List<String>,
    val remainMatchingUpdateTimeInSec: Long = System.currentTimeMillis() / SECOND_IN_MILLIS,
) {
    companion object {
        const val SECOND_IN_MILLIS: Long = 1000L
    }
}

fun getRemainingTimeInSec(startTimeInSec: Long): Long {
    val startTimeInMillis = startTimeInSec * SECOND_IN_MILLIS
    val zoneId = ZoneId.of("Asia/Seoul")
    val now = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startTimeInMillis), zoneId)
    val today10PM = now.withHour(22).withMinute(0).withSecond(0).withNano(0)
    val targetTime = if (now.isBefore(today10PM)) today10PM else today10PM.plusDays(1)
    return Duration.between(now, targetTime).seconds
}

/**
 * [매칭 API 명세](https://yapp25app3.atlassian.net/wiki/spaces/APP3/pages/17268751/API)
 */
enum class MatchStatus {
    // 자신이 매칭 조각 열람 전
    BEFORE_OPEN,

    // 자신은 매칭조각 열람, 상대는 매칭 수락 안함(열람했는지도 모름)
    WAITING,

    // 자신은 수락, 상대는 모름
    RESPONDED,

    // 자신은 열람만, 상대는 수락
    GREEN_LIGHT,

    // 둘다 수락
    MATCHED,

    // 내가 차단했을 때
    REFUSED,

    // 오류
    UNKNOWN;

    companion object {
        fun create(value: String?): MatchStatus {
            return MatchStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
