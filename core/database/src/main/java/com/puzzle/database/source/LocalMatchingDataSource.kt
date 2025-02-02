package com.puzzle.database.source

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.dao.ValuePicksDao
import com.puzzle.database.dao.ValueTalksDao
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMatchingDataSource @Inject constructor(
    private val valuePicksDao: ValuePicksDao,
    private val valueTalksDao: ValueTalksDao,
) {
    suspend fun retrieveValuePicks() = valuePicksDao.getValuePicks()
    suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>) = suspendRunCatching {
        valuePicks.forEach { valuePicksDao.replaceValuePicks(it) }
    }

    suspend fun retrieveValueTalks() = valueTalksDao.getValueTalks()
    suspend fun replaceValueTalks(valueTalks: List<ValueTalkEntity>) = suspendRunCatching {
        valueTalks.forEach { valueTalksDao.replaceValueTalks(it) }
    }
}
