package com.puzzle.data.fake.source.term

import com.puzzle.datastore.datasource.term.LocalTermDataSource
import com.puzzle.domain.model.terms.Term
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalTermDataSource : LocalTermDataSource {
    private var storedTerms: List<Term>? = emptyList()

    override val terms: Flow<List<Term>> = flow {
        storedTerms?.let { emit(it) }
            ?: throw NoSuchElementException("No value present in DataStore")
    }

    override suspend fun setTerms(terms: List<Term>) {
        storedTerms = terms
    }

    override suspend fun clearTerms() {
        storedTerms = emptyList()
    }
}
