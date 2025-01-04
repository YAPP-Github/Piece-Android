package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAuthCodeResponse(
    val status: String?,
    val message: String?,
    val data: Data?,
) {
    @Serializable
    data class Data(
        val smsVerified: Boolean?,
        val registerCompleted: Boolean?,
        val accessToken: String?,
        val refreshToken: String?,
    )
}
