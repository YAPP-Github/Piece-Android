package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RequestAuthCodeResponse(
    val status: String?,
    val message: String?,
    val data: String?,
)
