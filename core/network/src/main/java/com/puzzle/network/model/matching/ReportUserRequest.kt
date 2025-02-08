package com.puzzle.network.model.matching

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportUserRequest(
    @SerialName("reportedUserId") val userId: Int,
    val reason: String,
)
