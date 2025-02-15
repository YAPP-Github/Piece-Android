package com.puzzle.network.source.matching

import com.puzzle.network.model.matching.GetContactsResponse
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse

interface MatchingDataSource {
    suspend fun reportUser(userId: Int, reason: String): Result<Unit>
    suspend fun blockUser(userId: Int): Result<Unit>
    suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit>
    suspend fun getMatchInfo(): Result<GetMatchInfoResponse>
    suspend fun getContacts(): Result<GetContactsResponse>
    suspend fun getOpponentValueTalks(): Result<GetOpponentValueTalksResponse>
    suspend fun getOpponentValuePicks(): Result<GetOpponentValuePicksResponse>
    suspend fun getOpponentProfileBasic(): Result<GetOpponentProfileBasicResponse>
    suspend fun getOpponentProfileImage(): Result<GetOpponentProfileImageResponse>
    suspend fun checkMatchingPiece(): Result<Unit>
    suspend fun acceptMatching(): Result<Unit>
}
