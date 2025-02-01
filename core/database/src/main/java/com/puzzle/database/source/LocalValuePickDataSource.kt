package com.puzzle.database.source

import com.puzzle.database.dao.ValuePicksDao
import com.puzzle.database.model.matching.ValuePickEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalValuePickDataSource @Inject constructor(
    private val valuePicksDao: ValuePicksDao,
) {
    suspend fun retrieveValuePicks() = valuePicksDao.getValuePicks()
    suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>) =
        valuePicksDao.replaceValuePicks(*valuePicks.toTypedArray())
}
