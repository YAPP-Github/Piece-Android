package com.puzzle.network.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class PieceInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder()

        if (isAccessTokenUsed(originRequest)) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer ${runBlocking { tokenManager.getAccessToken() }}"
            )
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun isAccessTokenUsed(request: Request): Boolean {
        return when (request.url.encodedPath) {
            "/api/common/health" -> false
            "/api/login/oauth" -> false
            "/api/terms" -> false
            "/api/valueTalks" -> false
            "/api/valuePicks" -> false
            else -> true
        }
    }
}