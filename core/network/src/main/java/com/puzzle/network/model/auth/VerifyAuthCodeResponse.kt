package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAuthCodeResponse(
    val role: String?,
    val accessToken: String?,
    val refreshToken: String?,
)
