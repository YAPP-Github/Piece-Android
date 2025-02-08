package com.puzzle.network.source.matching

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.ReportUserRequest
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchingDataSource @Inject constructor(
    private val pieceApi: PieceApi
) {
    suspend fun reportUser(userId: Int, reason: String): Result<Unit> =
        pieceApi.reportUser(ReportUserRequest(userId = userId, reason = reason))
            .unwrapData()
}
