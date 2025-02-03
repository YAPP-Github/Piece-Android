package com.puzzle.database.source.matching

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.dao.ValuePicksDao
import com.puzzle.database.dao.ValueTalksDao
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMatchingDataSourceImpl @Inject constructor(
    private val valuePicksDao: ValuePicksDao,
    private val valueTalksDao: ValueTalksDao,
) : LocalMatchingDataSource {
    override suspend fun retrieveValuePicks() = valuePicksDao.getValuePicks()
    override suspend fun replaceValuePicks(valuePicks: List<ValuePickEntity>) = suspendRunCatching {
        valuePicks.forEach { valuePicksDao.replaceValuePicks(it) }
    }

    override suspend fun retrieveValueTalks() = valueTalksDao.getValueTalks()
    override suspend fun replaceValueTalks(valueTalks: List<ValueTalkEntity>) = suspendRunCatching {
        valueTalks.forEach { valueTalksDao.replaceValueTalks(it) }
    }
}
