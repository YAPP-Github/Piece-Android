package com.puzzle.network.model.profile

import com.puzzle.domain.model.profile.AiSummary
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class SseAiSummaryResponse(
    val profileValueTalkId: Int?,
    val summary: String?,
) {
    fun toDomain() = AiSummary(
        id = profileValueTalkId ?: UNKNOWN_INT,
        summary = summary ?: UNKNOWN_STRING,
    )
}
