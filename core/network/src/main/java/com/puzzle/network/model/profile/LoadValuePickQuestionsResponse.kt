package com.puzzle.network.model.profile

import com.puzzle.domain.model.profile.AnswerOption
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class LoadValuePickQuestionsResponse(
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
    fun toDomain() = ValuePickQuestion(
        id = id ?: UNKNOWN_INT,
        category = category ?: UNKNOWN_STRING,
        question = question ?: UNKNOWN_STRING,
        answerOptions = answers?.map(ValuePickAnswerResponse::toDomain) ?: emptyList()
    )
}

@Serializable
data class ValuePickAnswerResponse(
    val number: Int?,
    val content: String?,
) {
    fun toDomain() = AnswerOption(
        number = number ?: UNKNOWN_INT,
        content = content ?: UNKNOWN_STRING,
    )
}
