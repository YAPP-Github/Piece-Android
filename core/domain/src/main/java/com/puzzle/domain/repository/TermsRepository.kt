package com.puzzle.domain.repository

import com.puzzle.domain.model.terms.Term

interface TermsRepository {
    suspend fun loadTerms(): Result<Unit>
    suspend fun retrieveTerms(): Result<List<Term>>
    suspend fun agreeTerms(ids: List<Int>): Result<Unit>
}
