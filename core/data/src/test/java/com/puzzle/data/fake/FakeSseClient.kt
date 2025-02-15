package com.puzzle.data.fake

import com.puzzle.network.api.sse.SseClient
import com.puzzle.network.model.profile.SseAiSummaryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSseClient : SseClient {
    override val aiSummaryResponse: Flow<SseAiSummaryResponse> = flow { }

    override suspend fun connect(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun disconnect(): Result<Unit> {
        return Result.success(Unit)
    }
}
