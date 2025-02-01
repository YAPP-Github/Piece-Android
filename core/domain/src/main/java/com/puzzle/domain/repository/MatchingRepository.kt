package com.puzzle.domain.repository

import com.puzzle.domain.model.matching.ValuePick
import com.puzzle.domain.model.matching.ValueTalk

interface MatchingRepository {
    suspend fun loadValuePicks(): Result<Unit>
    suspend fun loadValueTalks(): Result<Unit>
    suspend fun retrieveValuePick(): Result<List<ValuePick>>
    suspend fun retrieveValueTalk(): Result<List<ValueTalk>>
}
