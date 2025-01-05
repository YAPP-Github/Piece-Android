package com.puzzle.data.repository

import com.puzzle.database.model.terms.TermEntity
import com.puzzle.database.source.term.LocalTermDataSource
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.network.source.TermDataSource
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termDataSource: TermDataSource,
    private val localTermDataSource: LocalTermDataSource,
) : TermsRepository {
    override suspend fun loadTerms(): Result<Unit> = runCatching {
        val terms = termDataSource.loadTerms()
            .getOrThrow()
            .toDomain()

        val termsEntity = terms.map {
            TermEntity(
                termId = it.termId,
                title = it.title,
                content = it.content,
                required = it.required,
                startDate = it.startDate.toString(),
            )
        }

        localTermDataSource.clearAndInsertTerms(termsEntity)
    }
}