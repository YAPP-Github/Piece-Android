package com.puzzle.datastore.datasource.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.puzzle.datastore.util.clear
import com.puzzle.datastore.util.getValue
import com.puzzle.datastore.util.setValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class LocalUserDataSourceImpl @Inject constructor(
    @Named("user") private val dataStore: DataStore<Preferences>
) {
    val userRole: Flow<String> = dataStore.getValue(USER_ROLE, "")

    suspend fun setUserRole(userRole: String) {
        dataStore.setValue(USER_ROLE, userRole)
    }

    suspend fun clearUserRole() {
        dataStore.clear(USER_ROLE)
    }

    companion object {
        private val USER_ROLE = stringPreferencesKey("USER_ROLE")
    }
}
