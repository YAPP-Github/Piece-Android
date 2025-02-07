package com.puzzle.network.model.matching

import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class LoadValueTalksResponse(
    val responses: List<ValueTalkResponse>?,
) {
    fun toDomain() = responses?.map(ValueTalkResponse::toDomain) ?: emptyList()
}

@Serializable
data class ValueTalkResponse(
    val id: Int?,
    val category: String?,
    val title: String?,
    val guide: String?,
) {
    fun toDomain() = ValueTalk(
        id = id ?: UNKNOWN_INT,
        category = category ?: UNKNOWN_STRING,
        title = title ?: UNKNOWN_STRING,
        helpMessages = emptyList() // Todo 서버에서 API 스키마 변경해줘야 함
    )
}
