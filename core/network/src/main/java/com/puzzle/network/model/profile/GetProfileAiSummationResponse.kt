package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class GetProfileAiSummationResponse(
    val profileValueTalkId: Int?,
    val summary: String?,
)
