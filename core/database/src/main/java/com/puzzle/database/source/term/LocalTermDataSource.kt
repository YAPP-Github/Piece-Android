package com.puzzle.database.source.term

import com.puzzle.database.dao.TermsDao
import com.puzzle.database.model.terms.TermEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTermDataSource @Inject constructor(
    private val termsDao: TermsDao,
) {
    suspend fun getTerms() = termsDao.getTerms()
    suspend fun clearAndInsertTerms(terms: List<TermEntity>) =
        termsDao.clearAndInsertTerms(*terms.toTypedArray())
}
