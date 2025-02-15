package com.puzzle.network.sse

import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.puzzle.common.suspendRunCatching
import javax.inject.Inject

class SseClient @Inject constructor(
    private val sseEventSource: BackgroundEventSource,
    private val sseEventHandler: SseEventHandler,
) {
    suspend fun connect(): Result<Unit> = suspendRunCatching {
        sseEventSource.start()
    }

    suspend fun disconnect(): Result<Unit> = suspendRunCatching {
        sseEventSource.close()
    }
}

