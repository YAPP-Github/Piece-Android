package com.puzzle.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSettingRequest(
    val toggle: Boolean,
)
