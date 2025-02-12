package com.puzzle.data.fake.source.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.network.model.profile.GetMyProfileBasicResponse
import com.puzzle.network.model.profile.GetMyValuePicksResponse
import com.puzzle.network.model.profile.GetMyValueTalksResponse
import com.puzzle.network.model.profile.LoadValuePickQuestionsResponse
import com.puzzle.network.model.profile.LoadValueTalkQuestionsResponse
import com.puzzle.network.model.profile.UploadProfileResponse
import com.puzzle.network.model.profile.ValuePickResponse
import com.puzzle.network.model.profile.ValueTalkResponse
import com.puzzle.network.source.profile.ProfileDataSource
import java.io.InputStream

class FakeProfileDataSource : ProfileDataSource {
    private var valuePickQuestions = listOf<ValuePickResponse>()
    private var valueTalkQuestions = listOf<ValueTalkResponse>()

    override suspend fun loadValuePickQuestions(): Result<LoadValuePickQuestionsResponse> =
        Result.success(LoadValuePickQuestionsResponse(responses = valuePickQuestions))

    override suspend fun loadValueTalkQuestions(): Result<LoadValueTalkQuestionsResponse> =
        Result.success(LoadValueTalkQuestionsResponse(responses = valueTalkQuestions))

    override suspend fun getMyProfileBasic(): Result<GetMyProfileBasicResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyValueTalks(): Result<GetMyValueTalksResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyValuePicks(): Result<GetMyValuePicksResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun checkNickname(nickname: String): Result<Boolean> =
        Result.success(true)

    override suspend fun uploadProfileImage(imageInputStream: InputStream): Result<String> =
        Result.success("")

    override suspend fun uploadProfile(
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
        valueTalks: List<ValueTalkAnswer>
    ): Result<UploadProfileResponse> = Result.success(
        UploadProfileResponse(
            role = "PENDING",
            accessToken = "accessToken",
            refreshToken = "refreshToken",
        )
    )

    fun setValuePicks(picks: List<ValuePickResponse>) {
        valuePickQuestions = picks
    }

    fun setValueTalks(talks: List<ValueTalkResponse>) {
        valueTalkQuestions = talks
    }
}
