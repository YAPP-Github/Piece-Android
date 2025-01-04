package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginOauthRequest(
    val providerName: String,
    val token: String,
)
