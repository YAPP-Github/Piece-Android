package com.puzzle.database.source.term

import com.puzzle.database.dao.TermDao
import com.puzzle.database.model.terms.TermEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTermDataSource @Inject constructor(
    private val termDao: TermDao,
) {
    suspend fun getTerms() = termDao.getTerms()
    suspend fun clearAndInsertTerms(terms: List<TermEntity>) =
        termDao.clearAndInsertTerms(*terms.toTypedArray())
}
