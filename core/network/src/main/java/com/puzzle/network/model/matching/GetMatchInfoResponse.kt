package com.puzzle.network.model.matching

import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING

data class GetMatchInfoResponse(
    val matchId: Int?,
    val matchStatus: String?,
    val description: String?,
    val nickname: String?,
    val birthYear: String?,
    val location: String?,
    val job: String?,
    val matchedValueCount: Int?,
    val matchedValueList: List<String>?,
) {
    fun toDomain() = MatchInfo(
        matchId = matchId ?: UNKNOWN_INT,
        matchStatus = matchStatus ?: UNKNOWN_STRING,
        description = description ?: "",
        nickname = nickname ?: UNKNOWN_STRING,
        birthYear = birthYear ?: UNKNOWN_STRING,
        location = location ?: UNKNOWN_STRING,
        job = job ?: UNKNOWN_STRING,
        matchedValueCount = matchedValueCount ?: UNKNOWN_INT,
        matchedValueList = matchedValueList ?: emptyList(),
    )
}
