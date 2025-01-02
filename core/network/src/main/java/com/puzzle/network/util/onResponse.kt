package com.puzzle.network.util

import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.domain.model.error.HttpResponseStatus
import retrofit2.Response

internal fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        return Result.success(body() ?: Unit as T)
    } else {
        errorBody()?.let {
            return Result.failure(
                HttpResponseException(
                    status = HttpResponseStatus.create(code()),
                    msg = "", // Todo
                )
            )
        } ?: return Result.failure(
            HttpResponseException(
                status = HttpResponseStatus.create(-1),
                msg = "알 수 없는 에러입니다."
            )
        )
    }
}
