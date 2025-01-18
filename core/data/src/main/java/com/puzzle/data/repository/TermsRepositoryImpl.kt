package com.puzzle.data.repository

import android.util.Log
import com.puzzle.common.suspendRunCatching
import com.puzzle.database.model.terms.TermEntity
import com.puzzle.database.source.term.LocalTermDataSource
import com.puzzle.domain.model.terms.Term
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.source.TermDataSource
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

        val termsEntity = terms.map {
            TermEntity(
                id = it.id,
                title = it.title,
                content = it.content,
                required = it.required,
                startDate = it.startDate,
            )
        }

        Log.d("test", termsEntity.toString())

        localTermDataSource.replaceTerms(termsEntity)
    }

    override suspend fun getTerms(): Result<List<Term>> = runCatching {
        localTermDataSource.retrieveTerms()
            .map(TermEntity::toDomain)
    }
}
