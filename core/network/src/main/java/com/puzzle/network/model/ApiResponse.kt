package com.puzzle.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: String?,
    val message: String?,
    val data: T?
)

const val UNKNOWN_INT = -1
const val UNKNOWN_STRING = "UNKNOWN"
