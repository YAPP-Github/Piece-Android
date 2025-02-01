package com.puzzle.database.source

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.dao.ValuePicksDao
import com.puzzle.database.model.matching.ValuePickEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalValuePickDataSource @Inject constructor(
    private val valuePicksDao: ValuePicksDao,
) {
    suspend fun retrieveValuePicks() = valuePicksDao.getValuePicks()
    suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>) = suspendRunCatching {
        valuePicks.forEach { valuePicksDao.replaceValuePicks(it) }
    }
}
