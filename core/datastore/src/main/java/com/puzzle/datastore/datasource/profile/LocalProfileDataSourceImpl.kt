package com.puzzle.datastore.datasource.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.puzzle.datastore.util.getValue
import com.puzzle.datastore.util.setValue
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocalProfileDataSourceImpl @Inject constructor(
    @Named("profile") private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) : LocalProfileDataSource {
    override val valuePickQuestions: Flow<List<ValuePickQuestion>> =
        dataStore.getValue(VALUE_PICK_QUESTIONS, "")
            .map { jsonString ->
                gson.fromJson(jsonString, object : TypeToken<List<ValuePickQuestion>>() {}.type)
            }

    override suspend fun setValuePickQuestions(valuePicks: List<ValuePickQuestion>) {
        val jsonString = gson.toJson(valuePicks)
        dataStore.setValue(VALUE_PICK_QUESTIONS, jsonString)
    }

    override val valueTalkQuestions: Flow<List<ValueTalkQuestion>> =
        dataStore.getValue(VALUE_TALK_QUESTIONS, "")
            .map { gson.fromJson(it, object : TypeToken<List<ValueTalkQuestion>>() {}.type) }

    override suspend fun setValueTalkQuestions(valueTalks: List<ValueTalkQuestion>) {
        val jsonString = gson.toJson(valueTalks)
        dataStore.setValue(VALUE_TALK_QUESTIONS, jsonString)
    }

    override val myProfile: Flow<MyProfile> = dataStore.getValue(MY_PROFILE, "")
        .map { gson.fromJson(it, MyProfile::class.java) }

    override suspend fun setMyProfile(profile: MyProfile) {
        val jsonString = gson.toJson(profile)
        dataStore.setValue(MY_PROFILE, jsonString)
    }

    override suspend fun clearMyProfile() {
        dataStore.edit { preferences -> preferences.remove(MY_PROFILE) }
    }

    companion object {
        private val VALUE_PICK_QUESTIONS = stringPreferencesKey("VALUE_PICK_QUESTIONS")
        private val VALUE_TALK_QUESTIONS = stringPreferencesKey("VALUE_TALK_QUESTIONS")
        private val MY_PROFILE = stringPreferencesKey("MY_PROFILE")
    }
}
