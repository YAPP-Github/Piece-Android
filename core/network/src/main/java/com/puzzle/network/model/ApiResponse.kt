package com.puzzle.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: String?,
    val message: String?,
    val data: T?,
)

@Serializable
data class ApiErrorResponse(
    val code: String?,
    val message: String?,
    val errors: List<ErrorResponse>? = emptyList(),
)

@Serializable
data class ErrorResponse(
    val field: String?,
    val message: String?,
)

internal fun <T> Result<ApiResponse<T>>.unwrapData(): Result<T> =
    this.mapCatching { response -> response.data ?: Unit as T }

const val UNKNOWN_INT = -1
const val UNKNOWN_STRING = "UNKNOWN"
