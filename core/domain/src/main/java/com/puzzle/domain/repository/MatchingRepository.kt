package com.puzzle.domain.repository

interface MatchingRepository {
    suspend fun loadValuePick(): Result<Unit>
    suspend fun loadValueTalk(): Result<Unit>
}
