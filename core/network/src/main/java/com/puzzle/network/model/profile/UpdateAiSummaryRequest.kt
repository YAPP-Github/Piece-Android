package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAiSummaryRequest(
    val summary: String,
)
