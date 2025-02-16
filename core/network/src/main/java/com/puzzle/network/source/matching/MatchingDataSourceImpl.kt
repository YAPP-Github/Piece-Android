package com.puzzle.network.source.matching

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.BlockContactsRequest
import com.puzzle.network.model.matching.GetContactsResponse
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
class MatchingDataSourceImpl @Inject constructor(
    private val pieceApi: PieceApi
) : MatchingDataSource {
    override suspend fun refuseMatch(): Result<Unit> =
        pieceApi.refuseMatch().unwrapData()

    override suspend fun reportUser(userId: Int, reason: String): Result<Unit> =
        pieceApi.reportUser(ReportUserRequest(userId = userId, reason = reason)).unwrapData()

    override suspend fun blockUser(userId: Int): Result<Unit> =
        pieceApi.blockUser(userId).unwrapData()

    override suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit> =
        pieceApi.blockContacts(BlockContactsRequest(phoneNumbers)).unwrapData()

    override suspend fun getMatchInfo(): Result<GetMatchInfoResponse> =
        pieceApi.getMatchInfo().unwrapData()

    override suspend fun getContacts(): Result<GetContactsResponse> =
        pieceApi.getMatchesContacts().unwrapData()

    override suspend fun getOpponentValueTalks(): Result<GetOpponentValueTalksResponse> =
        pieceApi.getOpponentValueTalks().unwrapData()

    override suspend fun getOpponentValuePicks(): Result<GetOpponentValuePicksResponse> =
        pieceApi.getOpponentValuePicks().unwrapData()

    override suspend fun getOpponentProfileBasic(): Result<GetOpponentProfileBasicResponse> =
        pieceApi.getOpponentProfileBasic().unwrapData()

    override suspend fun getOpponentProfileImage(): Result<GetOpponentProfileImageResponse> =
        pieceApi.getOpponentProfileImage().unwrapData()

    override suspend fun checkMatchingPiece(): Result<Unit> =
        pieceApi.checkMatchingPiece().unwrapData()

    override suspend fun acceptMatching(): Result<Unit> = pieceApi.acceptMatching().unwrapData()
}
