package com.puzzle.data.repository

import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.profile.OpponentProfileBasic
import com.puzzle.domain.model.profile.OpponentValuePick
import com.puzzle.domain.model.profile.OpponentValueTalk
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse
import com.puzzle.network.source.matching.MatchingDataSource
import javax.inject.Inject

class MatchingRepositoryImpl @Inject constructor(
    private val matchingDataSource: MatchingDataSource,
) : MatchingRepository {
    override suspend fun reportUser(userId: Int, reason: String): Result<Unit> =
        matchingDataSource.reportUser(userId = userId, reason = reason)

    override suspend fun blockUser(userId: Int): Result<Unit> = matchingDataSource.blockUser(userId)
    override suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit> =
        matchingDataSource.blockContacts(phoneNumbers)

    override suspend fun getMatchInfo(): Result<MatchInfo> = matchingDataSource.getMatchInfo()
        .mapCatching(GetMatchInfoResponse::toDomain)

    override suspend fun getOpponentValueTalks(): Result<List<OpponentValueTalk>> =
        matchingDataSource.getOpponentValueTalks()
            .mapCatching(GetOpponentValueTalksResponse::toDomain)

    override suspend fun getOpponentValuePicks(): Result<List<OpponentValuePick>> =
        matchingDataSource.getOpponentValuePicks()
            .mapCatching(GetOpponentValuePicksResponse::toDomain)

    override suspend fun getOpponentProfileBasic(): Result<OpponentProfileBasic> =
        matchingDataSource.getOpponentProfileBasic()
            .mapCatching(GetOpponentProfileBasicResponse::toDomain)

    override suspend fun getOpponentProfileImage(): Result<String> =
        matchingDataSource.getOpponentProfileImage()
            .mapCatching(GetOpponentProfileImageResponse::toDomain)

    override suspend fun checkMatchingPiece(): Result<Unit> =
        matchingDataSource.checkMatchingPiece()

    override suspend fun acceptMatching(): Result<Unit> = matchingDataSource.acceptMatching()
}
