package com.puzzle.network.model.profile

import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyValueTalksResponse(
    val response: List<MyValueTalkResponse>?,
) {
    fun toDomain() = response?.map(MyValueTalkResponse::toDomain) ?: emptyList()
}

@Serializable
data class MyValueTalkResponse(
    @SerialName("profileValueTalkId") val id: Int?,
    val title: String?,
    val category: String?,
    val answer: String?,
    val summary: String?,
) {
    fun toDomain() = MyValueTalk(
        id = id ?: UNKNOWN_INT,
        title = title ?: UNKNOWN_STRING,
        category = category ?: UNKNOWN_STRING,
        answer = answer ?: UNKNOWN_STRING,
        summary = summary ?: UNKNOWN_STRING,
    )
}
