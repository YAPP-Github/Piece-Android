package com.puzzle.data.fake.source.matching

import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity
import com.puzzle.database.source.matching.LocalMatchingDataSource

class FakeLocalMatchingDataSource : LocalMatchingDataSource {
    private var valuePicks = listOf<ValuePickEntity>()
    private var valueTalks = listOf<ValueTalkEntity>()

    override suspend fun retrieveValuePicks(): List<ValuePickEntity> {
        return valuePicks
    }

    override suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>): Result<Unit> {
        this.valuePicks = valuePicks
        return Result.success(Unit)
    }

    override suspend fun retrieveValueTalks(): List<ValueTalkEntity> {
        return valueTalks
    }

    override suspend fun replaceValueTalks(valueTalks: List<ValueTalkEntity>): Result<Unit> {
        this.valueTalks = valueTalks
        return Result.success(Unit)
    }
}
