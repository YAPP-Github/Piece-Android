package com.puzzle.network.source.matching

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchingDataSourceImpl @Inject constructor(
    private val pieceApi: PieceApi,
) : MatchingDataSource {
    override suspend fun loadValuePicks(): Result<LoadValuePicksResponse> =
        pieceApi.loadValuePicks().unwrapData()

    override suspend fun loadValueTalks(): Result<LoadValueTalksResponse> =
        pieceApi.loadValueTalks().unwrapData()
}
