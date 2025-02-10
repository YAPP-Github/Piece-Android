package com.puzzle.domain.repository

import com.puzzle.domain.model.match.MatchInfo

interface MatchingRepository {
    suspend fun reportUser(userId: Int, reason: String): Result<Unit>
    suspend fun blockUser(userId: Int): Result<Unit>
    suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit>
    suspend fun getMatchInfo(): Result<MatchInfo>
    suspend fun checkMatchingPiece(): Result<Unit>
    suspend fun acceptMatching(): Result<Unit>
}
