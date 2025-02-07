package com.puzzle.database.source.term

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.dao.TermsDao
import com.puzzle.database.model.terms.TermEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTermDataSourceImpl @Inject constructor(
    private val termsDao: TermsDao,
) : LocalTermDataSource {
    override suspend fun retrieveTerms() = termsDao.getTerms()
    override suspend fun replaceTerms(terms: List<TermEntity>) = suspendRunCatching {
        termsDao.replaceTerms(*terms.toTypedArray())
    }
}
