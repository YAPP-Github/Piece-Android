package com.puzzle.network.source.matching

import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse

interface MatchingDataSource {
    suspend fun loadValuePicks(): Result<LoadValuePicksResponse>
    suspend fun loadValueTalks(): Result<LoadValueTalksResponse>
}
