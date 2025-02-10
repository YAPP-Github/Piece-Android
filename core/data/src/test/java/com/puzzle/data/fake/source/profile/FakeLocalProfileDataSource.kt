package com.puzzle.data.fake.source.profile

import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity
import com.puzzle.database.source.profile.LocalProfileDataSource

class FakeLocalProfileDataSource : LocalProfileDataSource {
    private var valuePicks = listOf<ValuePickEntity>()
    private var valueTalks = listOf<ValueTalkEntity>()

    override suspend fun retrieveValuePickQuestions(): List<ValuePickEntity> {
        return valuePicks
    }

    override suspend fun replaceValuePickQuestions(valuePicks: List<ValuePickEntity>): Result<Unit> {
        this.valuePicks = valuePicks
        return Result.success(Unit)
    }

    override suspend fun retrieveValueTalkQuestions(): List<ValueTalkEntity> {
        return valueTalks
    }

    override suspend fun replaceValueTalkQuestions(valueTalks: List<ValueTalkEntity>): Result<Unit> {
        this.valueTalks = valueTalks
        return Result.success(Unit)
    }
}
