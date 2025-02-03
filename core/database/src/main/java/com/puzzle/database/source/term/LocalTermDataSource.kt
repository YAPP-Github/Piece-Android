package com.puzzle.database.source.term

import com.puzzle.database.model.terms.TermEntity

interface LocalTermDataSource {
    suspend fun retrieveTerms(): List<TermEntity>
    suspend fun replaceTerms(terms: List<TermEntity>): Result<Unit>
}
