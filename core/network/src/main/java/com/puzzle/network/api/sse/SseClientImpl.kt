package com.puzzle.network.api.sse

import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.puzzle.common.suspendRunCatching
import com.puzzle.network.BuildConfig
import com.puzzle.network.interceptor.TokenManager
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SseClientImpl @Inject constructor(
    private val sseEventHandler: SseEventHandler,
    private val tokenManager: TokenManager,
) : SseClient {
    private var sseEventSource: BackgroundEventSource? = null
    override val aiSummaryResponse = sseEventHandler.sseChannel

    override suspend fun connect(): Result<Unit> = suspendRunCatching {
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

    override suspend fun disconnect(): Result<Unit> = suspendRunCatching {
        sseEventSource?.close()
        sseEventSource = null
    }
}

