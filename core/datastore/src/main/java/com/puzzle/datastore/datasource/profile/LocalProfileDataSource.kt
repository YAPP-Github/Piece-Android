package com.puzzle.datastore.datasource.profile

import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion
import kotlinx.coroutines.flow.Flow

interface LocalProfileDataSource {
    val valuePickQuestions: Flow<List<ValuePickQuestion>>
    suspend fun setValuePickQuestions(valuePicks: List<ValuePickQuestion>)

    val valueTalkQuestions: Flow<List<ValueTalkQuestion>>
    suspend fun setValueTalkQuestions(valueTalks: List<ValueTalkQuestion>)

    val myProfile: Flow<MyProfile>
    suspend fun setMyProfile(profile: MyProfile)
    suspend fun clearMyProfile()
}
