package com.puzzle.network.source.term

import com.puzzle.network.model.terms.LoadTermsResponse

interface TermDataSource {
    suspend fun loadTerms(): Result<LoadTermsResponse>
    suspend fun agreeTerms(agreeTermsId: List<Int>): Result<Unit>
}
