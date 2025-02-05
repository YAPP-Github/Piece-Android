package com.puzzle.database.source.profile

import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity

interface LocalProfileDataSource {
    suspend fun retrieveValuePicks(): List<ValuePickEntity>
    suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>): Result<Unit>

    suspend fun retrieveValueTalks(): List<ValueTalkEntity>
    suspend fun replaceValueTalks(valueTalks: List<ValueTalkEntity>): Result<Unit>
}
