package com.puzzle.network.model.token

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val refreshToken: String,
    val accessToken: String,
)
