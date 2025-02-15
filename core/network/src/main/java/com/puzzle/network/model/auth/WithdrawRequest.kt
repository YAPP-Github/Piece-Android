package com.puzzle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class WithdrawRequest(
    val reason: String,
)
