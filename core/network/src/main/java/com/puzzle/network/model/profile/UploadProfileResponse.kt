package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UploadProfileResponse(
    val role: String?,
    val accessToken: String?,
    val refreshToken: String?,
)
