package com.puzzle.data.datasource.term

import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.terms.TermResponse
import com.puzzle.network.source.term.TermDataSource

class FakeTermDataSource : TermDataSource {
    private var terms = listOf<TermResponse>()

    override suspend fun loadTerms(): Result<LoadTermsResponse> {
        return Result.success(LoadTermsResponse(responses = terms))
    }

    fun setTerms(newTerms: List<TermResponse>) {
        terms = newTerms
    }
}
