package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAuthCodeResponse(
    val status: String?,
    val message: String?,
    val data: VerifyAuthCodeResponseData?,
)

@Serializable
data class VerifyAuthCodeResponseData(
    val smsVerified: Boolean?,
    val registerCompleted: Boolean?,
    val accessToken: String?,
    val refreshToken: String?,
)
