package com.puzzle.database.source.profile

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.dao.ValuePicksDao
import com.puzzle.database.dao.ValueTalksDao
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValueTalkEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalProfileDataSourceImpl @Inject constructor(
    private val valuePicksDao: ValuePicksDao,
    private val valueTalksDao: ValueTalksDao,
) : LocalProfileDataSource {
    override suspend fun retrieveValuePickQuestions() = valuePicksDao.getValuePicks()
    override suspend fun replaceValuePickQuestions(valuePicks: List<ValuePickEntity>) = suspendRunCatching {
        valuePicks.forEach { valuePicksDao.replaceValuePicks(it) }
    }

    override suspend fun retrieveValueTalkQuestions() = valueTalksDao.getValueTalks()
    override suspend fun replaceValueTalkQuestions(valueTalks: List<ValueTalkEntity>) = suspendRunCatching {
        valueTalks.forEach { valueTalksDao.replaceValueTalks(it) }
    }
}
