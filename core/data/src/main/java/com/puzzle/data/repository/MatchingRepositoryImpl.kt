package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.model.matching.ValuePickAnswer
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValuePickQuestion
import com.puzzle.database.source.LocalMatchingDataSource
import com.puzzle.domain.model.matching.ValuePick
import com.puzzle.domain.model.matching.ValueTalk
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.source.MatchingDataSource
import javax.inject.Inject

class MatchingRepositoryImpl @Inject constructor(
    private val matchingDataSource: MatchingDataSource,
    private val localMatchingDataSource: LocalMatchingDataSource,
) : MatchingRepository {
    override suspend fun loadValuePick(): Result<Unit> = suspendRunCatching {
        val valuePicks = matchingDataSource.loadValuePicks()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valuePickEntities = valuePicks.map { valuePick ->
            ValuePickEntity(
                valuePickQuestion = ValuePickQuestion(
                    id = valuePick.id,
                    category = valuePick.category,
                    question = valuePick.question,
                ),
                answers = valuePick.answers.map { answer ->
                    ValuePickAnswer(
                        questionsId = valuePick.id,
                        number = answer.number,
                        content = answer.content,
                    )
                }
            )
        }

        localMatchingDataSource.replaceValuePicks(valuePickEntities)
    }

    override suspend fun loadValueTalk(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveValuePick(): Result<List<ValuePick>> = suspendRunCatching {
        localMatchingDataSource.retrieveValuePicks()
            .map(ValuePickEntity::toDomain)
    }

    override suspend fun retrieveValueTalk(): Result<List<ValueTalk>> {
        TODO("Not yet implemented")
    }
}
