package com.puzzle.datastore.datasource.term

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.puzzle.datastore.util.getValue
import com.puzzle.datastore.util.setValue
import com.puzzle.domain.model.terms.Term
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocalTermDataSourceImpl @Inject constructor(
    @Named("term") private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) : LocalTermDataSource {
    override val terms: Flow<List<Term>> = dataStore.getValue(TERM, "")
        .map { jsonString ->
            try {
                gson.fromJson(jsonString, object : TypeToken<List<Term>>() {}.type)
            } catch (e: Exception) {
                emptyList()
            }
        }

    override suspend fun setTerms(terms: List<Term>) {
        val jsonString = gson.toJson(terms)
        dataStore.setValue(TERM, jsonString)
    }

    override suspend fun clearTerms() {
        dataStore.edit { preferences -> preferences.remove(TERM) }
    }

    companion object {
        private val TERM = stringPreferencesKey("TERM")
    }
}
