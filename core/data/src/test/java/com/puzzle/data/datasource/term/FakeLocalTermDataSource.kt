package com.puzzle.data.datasource.term

import com.puzzle.database.model.terms.TermEntity
import com.puzzle.database.source.term.LocalTermDataSource

class FakeLocalTermDataSource : LocalTermDataSource {
    private var terms = listOf<TermEntity>()

    override suspend fun retrieveTerms(): List<TermEntity> {
        return terms
    }

    override suspend fun replaceTerms(terms: List<TermEntity>): Result<Unit> {
        this.terms = terms
        return Result.success(Unit)
    }
}
