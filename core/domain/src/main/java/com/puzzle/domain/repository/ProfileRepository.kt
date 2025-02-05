package com.puzzle.domain.repository

import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.domain.model.profile.ValueTalk

interface ProfileRepository {
    suspend fun loadValuePicks(): Result<Unit>
    suspend fun loadValueTalks(): Result<Unit>
    suspend fun retrieveValuePick(): Result<List<ValuePick>>
    suspend fun retrieveValueTalk(): Result<List<ValueTalk>>
}
