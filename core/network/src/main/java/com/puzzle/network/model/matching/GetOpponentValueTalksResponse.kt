package com.puzzle.network.model.matching

import com.puzzle.domain.model.profile.OpponentValueTalk
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class GetOpponentValueTalksResponse(
    val matchId: Int?,
    val description: String?,
    val nickname: String?,
    val valueTalks: List<OpponentValueTalkResponse>?,
) {
    fun toDomain() = valueTalks?.map(OpponentValueTalkResponse::toDomain) ?: emptyList()
}

@Serializable
data class OpponentValueTalkResponse(
    val category: String?,
    val summary: String?,
    val answer: String?,
) {
    fun toDomain() = OpponentValueTalk(
        category = category ?: UNKNOWN_STRING,
        summary = summary ?: UNKNOWN_STRING,
        answer = answer ?: UNKNOWN_STRING,
    )
}
