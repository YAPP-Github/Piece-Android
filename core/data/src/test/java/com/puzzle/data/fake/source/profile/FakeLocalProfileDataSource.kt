package com.puzzle.data.fake.source.profile

import com.puzzle.datastore.datasource.profile.LocalProfileDataSource
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class FakeLocalProfileDataSource : LocalProfileDataSource {
    private val _valuePickQuestions = MutableStateFlow<List<ValuePickQuestion>>(emptyList())
    override val valuePickQuestions: Flow<List<ValuePickQuestion>> = _valuePickQuestions

    private val _valueTalkQuestions = MutableStateFlow<List<ValueTalkQuestion>>(emptyList())
    override val valueTalkQuestions: Flow<List<ValueTalkQuestion>> = _valueTalkQuestions

    private val _myProfile = MutableStateFlow<MyProfile?>(null)
    override val myProfile: Flow<MyProfile> = _myProfile.filterNotNull()

    override suspend fun setValuePickQuestions(valuePicks: List<ValuePickQuestion>) {
        _valuePickQuestions.value = valuePicks
    }

    override suspend fun setValueTalkQuestions(valueTalks: List<ValueTalkQuestion>) {
        _valueTalkQuestions.value = valueTalks
    }

    override suspend fun setMyProfile(profile: MyProfile) {
        _myProfile.value = profile
    }

    override suspend fun clearMyProfile() {
        _myProfile.value = null
    }
}
