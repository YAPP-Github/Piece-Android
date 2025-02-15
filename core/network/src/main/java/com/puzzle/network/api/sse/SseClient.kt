package com.puzzle.network.api.sse

import com.puzzle.network.model.profile.SseAiSummaryResponse
import kotlinx.coroutines.flow.Flow

interface SseClient {
    val aiSummaryResponse: Flow<SseAiSummaryResponse>
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
}
