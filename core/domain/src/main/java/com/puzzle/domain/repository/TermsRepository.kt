package com.puzzle.domain.repository

interface TermsRepository {
    suspend fun loadTerms(): Result<Unit>
}