package com.puzzle.database.source.matching

import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity

interface LocalMatchingDataSource {
    suspend fun retrieveValuePicks(): List<ValuePickEntity>
    suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>): Result<Unit>

    suspend fun retrieveValueTalks(): List<ValueTalkEntity>
    suspend fun replaceValueTalks(valueTalks: List<ValueTalkEntity>): Result<Unit>
}
