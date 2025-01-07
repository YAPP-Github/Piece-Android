package com.puzzle.network.model

import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.domain.model.error.HttpResponseStatus
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: String?,
    val message: String?,
    val data: T?,
)

internal fun <T> Result<ApiResponse<T>>.unwrapData(): Result<T> {
    return this.map { response ->
        response.data ?: return Result.failure(
            HttpResponseException(
                status = HttpResponseStatus.InternalError,
                msg = "일시적인 서버 에러입니다. 계속해서 반복될 경우 문의 하기를 이용해주세요.",
            )
        )
    }
}

const val UNKNOWN_INT = -1
const val UNKNOWN_STRING = "UNKNOWN"
