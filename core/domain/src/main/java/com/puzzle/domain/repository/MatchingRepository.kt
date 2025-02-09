package com.puzzle.domain.repository

interface MatchingRepository {
    suspend fun reportUser(userId: Int, reason: String): Result<Unit>
    suspend fun blockUser(userId: Int): Result<Unit>
}
