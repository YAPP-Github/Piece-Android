package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyAuthCodeRequest(
    val phoneNumber: String,
    val code: String,
)
