package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UploadProfileImageRequest(
    val file: String,
)
