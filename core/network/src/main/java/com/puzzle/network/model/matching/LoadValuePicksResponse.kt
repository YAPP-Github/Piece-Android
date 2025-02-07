package com.puzzle.network.model.matching

import com.puzzle.domain.model.profile.Answer
import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class LoadValuePicksResponse(
    val responses: List<ValuePickResponse>?,
) {
    fun toDomain() = responses?.map(ValuePickResponse::toDomain) ?: emptyList()
}

@Serializable
data class ValuePickResponse(
    val id: Int?,
    val category: String?,
    val question: String?,
    val answers: List<ValuePickAnswerResponse>?,
) {
    fun toDomain() = ValuePick(
        id = id ?: UNKNOWN_INT,
        category = category ?: UNKNOWN_STRING,
        question = question ?: UNKNOWN_STRING,
        answers = answers?.map(ValuePickAnswerResponse::toDomain) ?: emptyList()
    )
}

@Serializable
data class ValuePickAnswerResponse(
    val number: Int?,
    val content: String?,
) {
    fun toDomain() = Answer(
        number = number ?: UNKNOWN_INT,
        content = content ?: UNKNOWN_STRING,
    )
}
