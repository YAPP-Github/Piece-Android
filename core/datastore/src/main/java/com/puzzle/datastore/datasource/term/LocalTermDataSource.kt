package com.puzzle.datastore.datasource.term

import com.puzzle.domain.model.terms.Term
import kotlinx.coroutines.flow.Flow

interface LocalTermDataSource {
    val terms: Flow<List<Term>>
    suspend fun setTerms(terms: List<Term>)
    suspend fun clearTerms()
}
