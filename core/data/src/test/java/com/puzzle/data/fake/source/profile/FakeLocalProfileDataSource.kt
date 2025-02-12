package com.puzzle.data.fake.source.profile

import com.puzzle.datastore.datasource.profile.LocalProfileDataSource
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeLocalProfileDataSource : LocalProfileDataSource {
    private var _valuePickQuestions: List<ValuePickQuestion>? = null
    override val valuePickQuestions: Flow<List<ValuePickQuestion>>
        get() = flow {
            _valuePickQuestions?.let { emit(it) }
                ?: throw NoSuchElementException("No value present in DataStore")
        }

    private var _valueTalkQuestions: List<ValueTalkQuestion>? = null
    override val valueTalkQuestions: Flow<List<ValueTalkQuestion>>
        get() = flow {
            _valueTalkQuestions?.let { emit(it) }
                ?: throw NoSuchElementException("No value present in DataStore")
        }

    private var _myProfileBasic: MyProfileBasic? = null
    override val myProfileBasic: Flow<MyProfileBasic>
        get() = flow {
            _myProfileBasic?.let { emit(it) }
                ?: throw NoSuchElementException("No value present in DataStore")
        }

    private val _myValuePicks = MutableStateFlow<List<MyValuePick>>(emptyList())
    override val myValuePicks: Flow<List<MyValuePick>> = _myValuePicks.asStateFlow()

    private val _myValueTalks = MutableStateFlow<List<MyValueTalk>>(emptyList())
    override val myValueTalks: Flow<List<MyValueTalk>> = _myValueTalks.asStateFlow()

    override suspend fun setValuePickQuestions(valuePicks: List<ValuePickQuestion>) {
        _valuePickQuestions = valuePicks
    }

    override suspend fun setValueTalkQuestions(valueTalks: List<ValueTalkQuestion>) {
        _valueTalkQuestions = valueTalks
    }

    override suspend fun setMyProfileBasic(profileBasic: MyProfileBasic) {
        _myProfileBasic = profileBasic
    }

    override suspend fun setMyValuePicks(valuePicks: List<MyValuePick>) {
        _myValuePicks.value = valuePicks
    }

    override suspend fun setMyValueTalks(valueTalks: List<MyValueTalk>) {
        _myValueTalks.value = valueTalks
    }

    override suspend fun clearMyProfile() {
        _myProfileBasic = null
        _myValuePicks.value = emptyList()
        _myValueTalks.value = emptyList()
    }
}
