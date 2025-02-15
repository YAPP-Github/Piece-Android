package com.puzzle.domain.repository

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.profile.ValueTalkQuestion

interface ProfileRepository {
    suspend fun loadValuePickQuestions(): Result<Unit>
    suspend fun retrieveValuePickQuestion(): Result<List<ValuePickQuestion>>

    suspend fun loadValueTalkQuestions(): Result<Unit>
    suspend fun retrieveValueTalkQuestion(): Result<List<ValueTalkQuestion>>

    suspend fun loadMyProfileBasic(): Result<Unit>
    suspend fun retrieveMyProfileBasic(): Result<MyProfileBasic>

    suspend fun loadMyValueTalks(): Result<Unit>
    suspend fun retrieveMyValueTalks(): Result<List<MyValueTalk>>

    suspend fun loadMyValuePicks(): Result<Unit>
    suspend fun retrieveMyValuePicks(): Result<List<MyValuePick>>

    suspend fun updateMyValueTalks(valueTalks: List<MyValueTalk>): Result<List<MyValueTalk>>
    suspend fun updateMyValuePicks(valuePicks: List<MyValuePick>): Result<List<MyValuePick>>
    suspend fun updateMyProfileBasic(
        description: String,
        nickname: String,
        birthDate: String,
        height: Int,
        weight: Int,
        location: String,
        job: String,
        smokingStatus: String,
        snsActivityLevel: String,
        imageUrl: String,
        contacts: List<Contact>,
    ): Result<MyProfileBasic>

    suspend fun updateAiSummary(profileTalkId: Int, summary: String): Result<Unit>

    suspend fun checkNickname(nickname: String): Result<Boolean>
    suspend fun uploadProfile(
        birthdate: String,
        description: String,
        height: Int,
        weight: Int,
        imageUrl: String,
        job: String,
        location: String,
        nickname: String,
        smokingStatus: String,
        snsActivityLevel: String,
        contacts: List<Contact>,
        valuePicks: List<ValuePickAnswer>,
        valueTalks: List<ValueTalkAnswer>,
    ): Result<Unit>

    suspend fun connectSSE(): Result<Unit>
    suspend fun disconnectSSE(): Result<Unit>
}
