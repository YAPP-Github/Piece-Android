package com.puzzle.datastore.datasource.matching

import com.puzzle.domain.model.profile.OpponentProfile
import kotlinx.coroutines.flow.Flow

interface LocalMatchingDataSource {
    val opponentProfile: Flow<OpponentProfile>
    suspend fun setOpponentProfile(opponentProfile: OpponentProfile)
    suspend fun clearOpponentProfile()
}
