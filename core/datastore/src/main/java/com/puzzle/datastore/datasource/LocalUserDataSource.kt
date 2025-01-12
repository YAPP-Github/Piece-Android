package com.puzzle.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.puzzle.datastore.util.clear
import com.puzzle.datastore.util.getValue
import com.puzzle.datastore.util.setValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class LocalUserDataSource @Inject constructor(
    @Named("user") private val dataStore: DataStore<Preferences>
) {
    val userType: Flow<String> = dataStore.getValue(USER_TYPE, "")

    suspend fun setUserType(userRole: String) {
        dataStore.setValue(USER_TYPE, userRole)
    }

    suspend fun clearUserType() {
        dataStore.clear(USER_TYPE)
    }

    companion object {
        private val USER_TYPE = stringPreferencesKey("USER_TYPE")
    }
}