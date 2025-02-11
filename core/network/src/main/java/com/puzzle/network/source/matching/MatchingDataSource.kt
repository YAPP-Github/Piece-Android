package com.puzzle.network.source.matching

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.BlockContactsRequest
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse
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
    suspend fun getOpponentValueTalks(): Result<GetOpponentValueTalksResponse> =
        pieceApi.getOpponentValueTalks().unwrapData()

    suspend fun getOpponentValuePicks(): Result<GetOpponentValuePicksResponse> =
        pieceApi.getOpponentValuePicks().unwrapData()

    suspend fun getOpponentProfileBasic(): Result<GetOpponentProfileBasicResponse> =
        pieceApi.getOpponentProfileBasic().unwrapData()

    suspend fun getOpponentProfileImage(): Result<GetOpponentProfileImageResponse> =
        pieceApi.getOpponentProfileImage().unwrapData()

    suspend fun checkMatchingPiece(): Result<Unit> = pieceApi.checkMatchingPiece().unwrapData()
    suspend fun acceptMatching(): Result<Unit> = pieceApi.acceptMatching().unwrapData()

}
