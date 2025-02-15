package com.puzzle.network.source.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.network.model.profile.GetMyProfileBasicResponse
import com.puzzle.network.model.profile.GetMyValuePicksResponse
import com.puzzle.network.model.profile.GetMyValueTalksResponse
import com.puzzle.network.model.profile.LoadValuePickQuestionsResponse
import com.puzzle.network.model.profile.LoadValueTalkQuestionsResponse
import com.puzzle.network.model.profile.UpdateAiSummaryResponse
import com.puzzle.network.model.profile.UploadProfileResponse
import java.io.InputStream

interface ProfileDataSource {
    suspend fun loadValuePickQuestions(): Result<LoadValuePickQuestionsResponse>
    suspend fun loadValueTalkQuestions(): Result<LoadValueTalkQuestionsResponse>

    suspend fun getMyProfileBasic(): Result<GetMyProfileBasicResponse>
    suspend fun getMyValueTalks(): Result<GetMyValueTalksResponse>
    suspend fun getMyValuePicks(): Result<GetMyValuePicksResponse>

    suspend fun updateMyValueTalks(valueTalks: List<MyValueTalk>): Result<GetMyValueTalksResponse>
    suspend fun updateMyValuePicks(valuePicks: List<MyValuePick>): Result<GetMyValuePicksResponse>
    suspend fun updateAiSummary(
        profileTalkId: Int,
        summary: String
    ): Result<UpdateAiSummaryResponse>

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
    ): Result<GetMyProfileBasicResponse>

    suspend fun checkNickname(nickname: String): Result<Boolean>

    suspend fun uploadProfileImage(imageInputStream: InputStream): Result<String>
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
    ): Result<UploadProfileResponse>

    suspend fun connectSSE(): Result<Unit>
    suspend fun disconnectSSE(): Result<Unit>
}
