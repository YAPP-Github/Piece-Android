package com.puzzle.network.source.matching

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.BlockContactsRequest
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.ReportUserRequest
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchingDataSource @Inject constructor(
    private val pieceApi: PieceApi
) {
    suspend fun reportUser(userId: Int, reason: String): Result<Unit> =
        pieceApi.reportUser(ReportUserRequest(userId = userId, reason = reason)).unwrapData()

    suspend fun blockUser(userId: Int): Result<Unit> = pieceApi.blockUser(userId).unwrapData()

    suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit> =
        pieceApi.blockContacts(BlockContactsRequest(phoneNumbers)).unwrapData()

    suspend fun getMatchInfo(): Result<GetMatchInfoResponse> = pieceApi.getMatchInfo().unwrapData()

    suspend fun checkMatchingPiece(): Result<Unit> = pieceApi.checkMatchingPiece().unwrapData()
}
