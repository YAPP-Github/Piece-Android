package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginOauthResponse(
    val smsVerified: Boolean?,
    val registerCompleted: Boolean?,
    val accessToken: String?,
    val refreshToken: String?,
)
