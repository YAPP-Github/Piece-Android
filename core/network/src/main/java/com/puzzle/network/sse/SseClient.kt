package com.puzzle.network.sse

import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.puzzle.common.suspendRunCatching
import com.puzzle.network.BuildConfig
import com.puzzle.network.api.PieceApi
import com.puzzle.network.interceptor.TokenManager
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SseClient @Inject constructor(
    private val sseEventHandler: BackgroundEventHandler,
    private val tokenManager: TokenManager,
    private val pieceApi: PieceApi,
) {
    private var sseEventSource: BackgroundEventSource? = null

    suspend fun connect(): Result<Unit> = suspendRunCatching {
        sseEventSource = BackgroundEventSource.Builder(
            sseEventHandler,
            EventSource.Builder(
                ConnectStrategy
                    .http(URL(BuildConfig.PIECE_BASE_URL + "/api/sse/personal/connect"))
                    .header(
                        "Authorization",
                        "Bearer ${tokenManager.getAccessToken()}"
                    )
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(600, TimeUnit.SECONDS)
            )
        ).build()

        sseEventSource?.start()
    }

    suspend fun disconnect(): Result<Unit> = suspendRunCatching {
        sseEventSource?.close()
        sseEventSource = null
        pieceApi.disconnectSSE()
    }
}

