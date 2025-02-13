package com.puzzle.network.model.matching

import com.puzzle.domain.model.profile.OpponentValuePick
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import com.puzzle.network.model.profile.ValuePickAnswerResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetOpponentValuePicksResponse(
    val matchId: Int?,
    val description: String?,
    val nickname: String?,
    val valuePicks: List<OpponentValuePickResponse>?,
) {
    fun toDomain() = valuePicks?.map(OpponentValuePickResponse::toDomain) ?: emptyList()
}

@Serializable
data class OpponentValuePickResponse(
    val category: String?,
    val question: String?,
    @SerialName("sameWithMe") val isSameWithMe: Boolean?,
    @SerialName("answer") val answerOptions: List<ValuePickAnswerResponse>?,
    @SerialName("answerNumber") val selectedAnswer: Int?,
) {
    fun toDomain() = OpponentValuePick(
        category = category ?: UNKNOWN_STRING,
        question = question ?: UNKNOWN_STRING,
        isSameWithMe = isSameWithMe ?: false,
        answerOptions = answerOptions?.map(ValuePickAnswerResponse::toDomain) ?: emptyList(),
        selectedAnswer = selectedAnswer ?: UNKNOWN_INT,
    )
}
