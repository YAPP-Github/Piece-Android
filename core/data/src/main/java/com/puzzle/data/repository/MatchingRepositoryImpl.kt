package com.puzzle.data.repository

import com.puzzle.domain.model.matching.ValuePick
import com.puzzle.domain.model.matching.ValueTalk
import com.puzzle.domain.repository.MatchingRepository
import javax.inject.Inject

class MatchingRepositoryImpl @Inject constructor(

) : MatchingRepository {
    override suspend fun loadValuePick(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun loadValueTalk(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveValuePick(): Result<List<ValuePick>> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveValueTalk(): Result<List<ValueTalk>> {
        TODO("Not yet implemented")
    }
}
