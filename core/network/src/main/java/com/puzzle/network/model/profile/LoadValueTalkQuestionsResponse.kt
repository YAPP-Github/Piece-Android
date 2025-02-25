package com.puzzle.network.model.profile

import com.puzzle.domain.model.profile.ValueTalkQuestion
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class LoadValueTalkQuestionsResponse(
    val responses: List<ValueTalkResponse>?,
) {
    fun toDomain() = responses?.map(ValueTalkResponse::toDomain) ?: emptyList()
}

@Serializable
data class ValueTalkResponse(
    val id: Int?,
    val category: String?,
    val title: String?,
    val placeholder: String?,
    val guides: List<String>?,
) {
    fun toDomain() = ValueTalkQuestion(
        id = id ?: UNKNOWN_INT,
        category = category ?: UNKNOWN_STRING,
        title = title ?: UNKNOWN_STRING,
        placeholder = placeholder ?: UNKNOWN_STRING,
        guides = guides ?: emptyList(),
    )
}
