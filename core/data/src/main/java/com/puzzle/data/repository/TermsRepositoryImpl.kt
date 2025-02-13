package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.datastore.datasource.term.LocalTermDataSource
import com.puzzle.domain.model.terms.Term
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.source.term.TermDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termDataSource: TermDataSource,
    private val localTermDataSource: LocalTermDataSource,
) : TermsRepository {
    override suspend fun loadTerms(): Result<Unit> = suspendRunCatching {
        val terms = termDataSource.loadTerms()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        localTermDataSource.setTerms(terms)
    }

    override suspend fun retrieveTerms(): Result<List<Term>> = suspendRunCatching {
        localTermDataSource.terms.first()
    }

    override suspend fun agreeTerms(ids: List<Int>): Result<Unit> = termDataSource.agreeTerms(ids)
}
