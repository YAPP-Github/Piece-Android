package com.puzzle.data.fake.source.profile

import com.puzzle.datastore.datasource.profile.LocalProfileDataSource
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalProfileDataSource : LocalProfileDataSource {
    private var _valuePickQuestions: List<ValuePickQuestion>? = null
    override val valuePickQuestions: Flow<List<ValuePickQuestion>> = flow {
        _valuePickQuestions?.let { emit(it) }
            ?: throw NoSuchElementException("No value present in DataStore")
    }

    private var _valueTalkQuestions: List<ValueTalkQuestion>? = null
    override val valueTalkQuestions: Flow<List<ValueTalkQuestion>> =
        flow {
            _valueTalkQuestions?.let { emit(it) }
                ?: throw NoSuchElementException("No value present in DataStore")
        }

    private var _myProfile: MyProfile? = null
    override val myProfile: Flow<MyProfile> = flow {
        _myProfile?.let { emit(it) }
            ?: throw NoSuchElementException("No value present in DataStore")
    }

    override suspend fun setValuePickQuestions(valuePicks: List<ValuePickQuestion>) {
        _valuePickQuestions = valuePicks
    }

    override suspend fun setValueTalkQuestions(valueTalks: List<ValueTalkQuestion>) {
        _valueTalkQuestions = valueTalks
    }

    override suspend fun setMyProfile(profile: MyProfile) {
        _myProfile = profile
    }

    override suspend fun clearMyProfile() {
        _myProfile = null
    }
}
