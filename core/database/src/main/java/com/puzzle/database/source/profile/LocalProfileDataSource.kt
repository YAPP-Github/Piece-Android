package com.puzzle.database.source.profile

import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity

interface LocalProfileDataSource {
    suspend fun retrieveValuePickQuestions(): List<ValuePickEntity>
    suspend fun replaceValuePickQuestions(valuePicks: List<ValuePickEntity>): Result<Unit>

    suspend fun retrieveValueTalkQuestions(): List<ValueTalkEntity>
    suspend fun replaceValueTalkQuestions(valueTalks: List<ValueTalkEntity>): Result<Unit>
}
