package com.puzzle.network.model.profile

import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAiSummaryResponse(
    val summary: String?,
) {
    fun toDomain(): String = summary ?: UNKNOWN_STRING
}
