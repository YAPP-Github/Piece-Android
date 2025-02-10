package com.puzzle.domain.model.match

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
)

/**
 * [매칭 API 명세](https://yapp25app3.atlassian.net/wiki/spaces/APP3/pages/17268751/API)
 */
enum class MatchStatus {
    BEFORE_OPEN,
    WAITING,
    RESPONDED,
    GREEN_LIGHT,
    MATCHED,
    UNKNOWN;

    companion object {
        fun create(value: String?): MatchStatus {
            return MatchStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
