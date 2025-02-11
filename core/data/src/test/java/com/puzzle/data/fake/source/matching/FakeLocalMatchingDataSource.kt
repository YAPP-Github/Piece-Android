package com.puzzle.data.fake.source.matching

import com.puzzle.datastore.datasource.matching.LocalMatchingDataSource
import com.puzzle.domain.model.profile.OpponentProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalMatchingDataSource : LocalMatchingDataSource {
    private var storedOpponentProfile: OpponentProfile? = null

    override val opponentProfile: Flow<OpponentProfile> = flow {
        storedOpponentProfile?.let { emit(it) }
            ?: throw NoSuchElementException("No value present in DataStore")
    }

    override suspend fun setOpponentProfile(profile: OpponentProfile) {
        storedOpponentProfile = profile
    }

    override suspend fun clearOpponentProfile() {}
}
