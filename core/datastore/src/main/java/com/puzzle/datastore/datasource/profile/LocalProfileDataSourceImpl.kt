package com.puzzle.datastore.datasource.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.puzzle.datastore.util.getValue
import com.puzzle.datastore.util.setValue
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
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
                try {
                    gson.fromJson(jsonString, object : TypeToken<List<ValuePickQuestion>>() {}.type)
                } catch (e: Exception) {
                    emptyList()
                }
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

    override val myProfileBasic: Flow<MyProfileBasic> = dataStore.getValue(MY_PROFILE_BASIC, "")
        .map { gson.fromJson(it, MyProfileBasic::class.java) }

    override suspend fun setMyProfileBasic(profileBasic: MyProfileBasic) {
        val jsonString = gson.toJson(profileBasic)
        dataStore.setValue(MY_PROFILE_BASIC, jsonString)
    }

    override val myValuePicks: Flow<List<MyValuePick>> = dataStore.getValue(MY_VALUE_PICKS, "")
        .map { gson.fromJson(it, object : TypeToken<List<MyValuePick>>() {}.type) }


    override suspend fun setMyValuePicks(valuePicks: List<MyValuePick>) {
        val jsonString = gson.toJson(valuePicks)
        dataStore.setValue(MY_VALUE_PICKS, jsonString)
    }

    override val myValueTalks: Flow<List<MyValueTalk>> = dataStore.getValue(MY_VALUE_TALKS, "")
        .map { gson.fromJson(it, object : TypeToken<List<MyValueTalk>>() {}.type) }

    override suspend fun setMyValueTalks(valueTalks: List<MyValueTalk>) {
        val jsonString = gson.toJson(valueTalks)
        dataStore.setValue(MY_VALUE_TALKS, jsonString)
    }

    override suspend fun clearMyProfile() {
        dataStore.edit { preferences ->
            preferences.remove(MY_PROFILE_BASIC)
            preferences.remove(MY_VALUE_TALKS)
            preferences.remove(MY_VALUE_PICKS)
        }
    }

    companion object {
        private val VALUE_PICK_QUESTIONS = stringPreferencesKey("VALUE_PICK_QUESTIONS")
        private val VALUE_TALK_QUESTIONS = stringPreferencesKey("VALUE_TALK_QUESTIONS")
        private val MY_PROFILE_BASIC = stringPreferencesKey("MY_PROFILE_BASIC")
        private val MY_VALUE_PICKS = stringPreferencesKey("MY_VALUE_PICKS")
        private val MY_VALUE_TALKS = stringPreferencesKey("MY_VALUE_TALKS")
    }
}
