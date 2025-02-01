package com.puzzle.network.authenticator

import com.puzzle.domain.model.error.HttpResponseStatus
import com.puzzle.network.api.PieceApi
import com.puzzle.network.interceptor.TokenManager
import com.puzzle.network.model.token.RefreshTokenRequest
import com.puzzle.network.model.unwrapData
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class PieceAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val pieceApi: Provider<PieceApi>,
) : Authenticator {
    private val lock = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request

        if (originRequest.header("Authorization").isNullOrEmpty()) {
            return null
        }

        if (originRequest.url.encodedPath.contains("/api/v1/auth/common/refresh")) {
            runBlocking {
                tokenManager.setAccessToken("")
                tokenManager.setRefreshToken("")
            }
            return null
        }

        val retryCount = originRequest.header(RETRY_HEADER)?.toIntOrNull() ?: 0
        if (retryCount >= MAX_RETRY_COUNT) {
            return null
        }

        if (response.code != HttpResponseStatus.Unauthorized.code) {
            return null
        }

        val token = runBlocking {
            lock.withLock {
                pieceApi.get()
                    .refreshToken(RefreshTokenRequest(tokenManager.getRefreshToken()))
                    .unwrapData()
            }
        }.getOrNull() ?: return null

        runBlocking {
            val job = launch { tokenManager.setRefreshToken(token.refreshToken) }
            tokenManager.setAccessToken(token.accessToken)
            job.join()
        }

        val newRequest = response.request
            .newBuilder()
            .header("Authorization", "Bearer ${token.accessToken}")
            .header(RETRY_HEADER, (retryCount + 1).toString())
            .build()

        return newRequest
    }

    companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val RETRY_HEADER = "Retry-Count"
    }
}
