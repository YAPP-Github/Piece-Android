package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginOauthResponse(
    val status: String?,
    val message: String?,
    val data: LoginOauthResponseData?,
)

@Serializable
data class LoginOauthResponseData(
    val smsVerified: Boolean?,
    val registerCompleted: Boolean?,
    val accessToken: String?,
    val refreshToken: String?,
)
