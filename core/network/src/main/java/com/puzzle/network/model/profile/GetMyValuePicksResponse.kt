package com.puzzle.network.model.profile

import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyValuePicksResponse(
    val response: List<MyValuePickResponse>?,
) {
    fun toDomain() = response?.map(MyValuePickResponse::toDomain) ?: emptyList()
}

@Serializable
data class MyValuePickResponse(
    @SerialName("profileValuePickId") val id: Int?,
    val category: String?,
    val question: String?,
    @SerialName("answer") val answerOptions: List<ValuePickAnswerResponse>?,
    val selectedAnswer: Int?,
) {
    fun toDomain() = MyValuePick(
        id = id ?: UNKNOWN_INT,
        category = category ?: UNKNOWN_STRING,
        question = question ?: UNKNOWN_STRING,
        answerOptions = answerOptions?.map(ValuePickAnswerResponse::toDomain) ?: emptyList(),
        selectedAnswer = selectedAnswer ?: UNKNOWN_INT,
    )
}
