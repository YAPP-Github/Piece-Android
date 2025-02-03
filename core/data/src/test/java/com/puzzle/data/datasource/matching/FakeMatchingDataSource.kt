package com.puzzle.data.datasource.matching

import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.matching.ValuePickResponse
import com.puzzle.network.model.matching.ValueTalkResponse
import com.puzzle.network.source.matching.MatchingDataSource

class FakeMatchingDataSource : MatchingDataSource {
    private var valuePicks = listOf<ValuePickResponse>()
    private var valueTalks = listOf<ValueTalkResponse>()

    override suspend fun loadValuePicks(): Result<LoadValuePicksResponse> {
        return Result.success(LoadValuePicksResponse(responses = valuePicks))
    }

    override suspend fun loadValueTalks(): Result<LoadValueTalksResponse> {
        return Result.success(LoadValueTalksResponse(responses = valueTalks))
    }

    fun setValuePicks(picks: List<ValuePickResponse>) {
        valuePicks = picks
    }

    fun setValueTalks(talks: List<ValueTalkResponse>) {
        valueTalks = talks
    }
}
