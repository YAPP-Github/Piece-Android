package com.puzzle.network.adapter

import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.domain.model.error.HttpResponseStatus
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PieceCallAdapterFactory @Inject constructor() : CallAdapter.Factory() {
    override fun get(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // 반환 타입의 최상위 객체가 Result 객체인지 확인, 아닐 경우 null 반환
        val wrapperType = getParameterUpperBound(0, type as ParameterizedType)
        if (getRawType(wrapperType) != Result::class.java) return null

        // 해당 타입의 제네릭 타입을 가져옴 Result<T>의 T를 뜻함
        val responseType = getParameterUpperBound(0, wrapperType as ParameterizedType)
        return PieceCallAdapter(responseType)
    }
}

private class PieceCallAdapter(
    private val resultType: Type,
) : CallAdapter<Type, Call<Result<Type>>> {
    // 반환 타입은 Result<T>의 T임
    override fun responseType(): Type = resultType

    // Call 반환의 응답을 CallAdapter로 매핑 시킴.
    override fun adapt(call: Call<Type>): Call<Result<Type>> = PieceCall(call)
}

private class PieceCall<T : Any>(
    private val delegate: Call<T>,
) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@PieceCall,
                    Response.success(response.toResult())
                )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback.onResponse(
                    this@PieceCall,
                    Response.success(Result.failure(throwable as Exception))
                )
            }

            private fun <T> Response<T>.toResult(): Result<T> {
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
        })
    }

    override fun execute(): Response<Result<T>> =
        throw UnsupportedOperationException("PieceCall doesn't support execute")

    override fun clone(): Call<Result<T>> = PieceCall(delegate.clone())
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
    override fun cancel() = delegate.cancel()
}