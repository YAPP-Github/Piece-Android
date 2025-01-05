package com.puzzle.data.repository

import com.puzzle.domain.repository.TermsRepository
import com.puzzle.network.source.TermsDataSource
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val termsDataSource: TermsDataSource,
) : TermsRepository {
    override suspend fun loadTerms(): Result<Unit> {
        termsDataSource.loadTerms()
        return Result.success(Unit)
    }
}