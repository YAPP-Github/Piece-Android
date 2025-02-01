package com.puzzle.network.source

import com.puzzle.network.api.PieceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchingDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun loadValuePick(): Result<Unit> = Result.success(Unit)
    suspend fun loadValueTalk(): Result<Unit> = Result.success(Unit)
}
