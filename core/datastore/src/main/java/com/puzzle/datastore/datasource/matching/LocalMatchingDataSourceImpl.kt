package com.puzzle.datastore.datasource.matching

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.puzzle.datastore.util.getValue
import com.puzzle.datastore.util.setValue
import com.puzzle.domain.model.profile.OpponentProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class LocalMatchingDataSourceImpl @Inject constructor(
    @Named("matching") private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) : LocalMatchingDataSource {
    override val opponentProfile: Flow<OpponentProfile?> = dataStore.getValue(OPPONENT_PROFILE, "")
        .map {
            try {
                gson.fromJson(it, OpponentProfile::class.java)
            } catch (e: Exception) {
                null
            }
        }

    override suspend fun setOpponentProfile(opponentProfile: OpponentProfile) {
        val jsonString = gson.toJson(opponentProfile)
        dataStore.setValue(OPPONENT_PROFILE, jsonString)
    }

    override suspend fun clearOpponentProfile() {
        dataStore.edit { preferences -> preferences.remove(OPPONENT_PROFILE) }
    }

    companion object {
        private val OPPONENT_PROFILE = stringPreferencesKey("OPPONENT_PROFILE")
    }
}
