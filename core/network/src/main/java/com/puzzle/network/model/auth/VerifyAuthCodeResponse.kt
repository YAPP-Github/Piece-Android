package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAuthCodeResponse(
    val smsVerified: Boolean?,
    val registerCompleted: Boolean?,
    val accessToken: String?,
    val refreshToken: String?,
)
