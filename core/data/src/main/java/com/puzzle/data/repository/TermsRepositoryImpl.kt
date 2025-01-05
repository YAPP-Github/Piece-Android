package com.puzzle.data.repository

import android.util.Log
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
    override suspend fun loadTerms(): Result<Unit> = runCatching {
        Log.d("test", "loadTerms 호출")

        val terms = termDataSource.loadTerms()
            .getOrThrow()
            .toDomain()
            .filter { it.termId != UNKNOWN_INT }

        Log.d("test", terms.toString())

        val termsEntity = terms.map {
            TermEntity(
                termId = it.termId,
                title = it.title,
                content = it.content,
                required = it.required,
                startDate = it.startDate.toString(),
            )
        }

        Log.d("test", termsEntity.toString())
        
        localTermDataSource.clearAndInsertTerms(termsEntity)
    }

    override suspend fun getTerms(): Result<List<Term>> = runCatching {
        localTermDataSource.getTerms()
            .map { it.toDomain() }
    }
}
