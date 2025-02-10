package com.puzzle.domain.model.match

data class MatchInfo(
    val matchId: Int,
    val matchStatus: String,
    val description: String,
    val nickname: String,
    val birthYear: String,
    val location: String,
    val job: String,
    val matchedValueCount: Int,
    val matchedValueList: List<String>,
)
