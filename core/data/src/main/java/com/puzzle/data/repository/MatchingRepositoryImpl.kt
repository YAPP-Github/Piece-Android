package com.puzzle.data.repository

import com.puzzle.domain.repository.MatchingRepository
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
}
