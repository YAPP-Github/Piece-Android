package com.puzzle.datastore.datasource.profile

import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion
import kotlinx.coroutines.flow.Flow

interface LocalProfileDataSource {
    val valuePickQuestions: Flow<List<ValuePickQuestion>>
    suspend fun setValuePickQuestions(valuePicks: List<ValuePickQuestion>)

    val valueTalkQuestions: Flow<List<ValueTalkQuestion>>
    suspend fun setValueTalkQuestions(valueTalks: List<ValueTalkQuestion>)

    val myProfileBasic: Flow<MyProfileBasic>
    suspend fun setMyProfileBasic(profileBasic: MyProfileBasic)

    val myValuePicks: Flow<List<MyValuePick>>
    suspend fun setMyValuePicks(valuePicks: List<MyValuePick>)

    val myValueTalks: Flow<List<MyValueTalk>>
    suspend fun setMyValueTalks(valueTalks: List<MyValueTalk>)

    suspend fun clearMyProfile()
}
