package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginOauthResponse(
    val role: String?,
    val accessToken: String?,
    val refreshToken: String?,
)
