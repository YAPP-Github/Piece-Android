package com.puzzle.data.repository

import com.puzzle.domain.repository.TermsRepository
import com.puzzle.network.source.TermDataSource
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termDataSource: TermDataSource,
    private val localTermDataSource: TermDataSource,
) : TermsRepository {
    override suspend fun loadTerms(): Result<Unit> = runCatching {
        val terms = termDataSource.loadTerms().getOrThrow()
        localTermDataSource
    }
}