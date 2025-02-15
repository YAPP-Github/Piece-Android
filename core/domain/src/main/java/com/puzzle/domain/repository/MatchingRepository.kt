package com.puzzle.domain.repository

import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.OpponentProfile

interface MatchingRepository {
    suspend fun reportUser(userId: Int, reason: String): Result<Unit>
    suspend fun getContacts(): Result<List<Contact>>
    suspend fun blockUser(userId: Int): Result<Unit>
    suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit>
    suspend fun getMatchInfo(): Result<MatchInfo>
    suspend fun retrieveOpponentProfile(): Result<OpponentProfile>
    suspend fun loadOpponentProfile(): Result<Unit>
    suspend fun checkMatchingPiece(): Result<Unit>
    suspend fun acceptMatching(): Result<Unit>
}
