package com.puzzle.network.source

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchingDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun loadValuePicks(): Result<LoadValuePicksResponse> =
        pieceApi.loadValuePicks().unwrapData()

    suspend fun loadValueTalks(): Result<LoadValueTalksResponse> =
        pieceApi.loadValueTalks().unwrapData()
}
