package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class GenerateProfileResponse(
    val role: String?,
    val accessToken: String?,
    val refreshToken: String?,
)
