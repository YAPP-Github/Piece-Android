package com.puzzle.network.source.term

import com.puzzle.network.model.terms.LoadTermsResponse

interface TermDataSource {
    suspend fun loadTerms(): Result<LoadTermsResponse>
}
