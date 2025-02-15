package com.puzzle.data.fake.source.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.network.model.profile.ContactResponse
import com.puzzle.network.model.profile.GetMyProfileBasicResponse
import com.puzzle.network.model.profile.GetMyValuePicksResponse
import com.puzzle.network.model.profile.GetMyValueTalksResponse
import com.puzzle.network.model.profile.LoadValuePickQuestionsResponse
import com.puzzle.network.model.profile.LoadValueTalkQuestionsResponse
import com.puzzle.network.model.profile.MyValuePickResponse
import com.puzzle.network.model.profile.MyValueTalkResponse
import com.puzzle.network.model.profile.UploadProfileResponse
import com.puzzle.network.model.profile.ValuePickAnswerResponse
import com.puzzle.network.model.profile.ValuePickResponse
import com.puzzle.network.model.profile.ValueTalkResponse
import com.puzzle.network.source.profile.ProfileDataSource
import java.io.InputStream

class FakeProfileDataSource : ProfileDataSource {
    private var valuePicks = listOf<ValuePickResponse>()
    private var valueTalks = listOf<ValueTalkResponse>()

    override suspend fun loadValuePickQuestions(): Result<LoadValuePickQuestionsResponse> =
        Result.success(LoadValuePickQuestionsResponse(responses = valuePicks))

    override suspend fun loadValueTalkQuestions(): Result<LoadValueTalkQuestionsResponse> =
        Result.success(LoadValueTalkQuestionsResponse(responses = valueTalks))

    override suspend fun getMyProfileBasic(): Result<GetMyProfileBasicResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyValueTalks(): Result<GetMyValueTalksResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyValuePicks(): Result<GetMyValuePicksResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMyValueTalks(valueTalks: List<MyValueTalk>): Result<GetMyValueTalksResponse> {
        val responses = valueTalks.map { valueTalk ->
            MyValueTalkResponse(
                id = valueTalk.id,
                title = valueTalk.title,
                category = valueTalk.category,
                answer = valueTalk.answer,
                summary = valueTalk.summary,
                guides = valueTalk.guides,
            )
        }

        return Result.success(GetMyValueTalksResponse(responses = responses))
    }

    override suspend fun updateMyValuePicks(valuePicks: List<MyValuePick>): Result<GetMyValuePicksResponse> {
        val responses = valuePicks.map { valuePick ->
            MyValuePickResponse(
                id = valuePick.id,
                category = valuePick.category,
                question = valuePick.question,
                answerOptions = valuePick.answerOptions.map {
                    ValuePickAnswerResponse(
                        number = it.number,
                        content = it.content,
                    )
                },
                selectedAnswer = valuePick.selectedAnswer
            )
        }

        return Result.success(GetMyValuePicksResponse(responses = responses))
    }

    override suspend fun updateAiSummary(
        profileTalkId: Int,
        summary: String
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateMyProfileBasic(
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
        contacts: List<Contact>
    ): Result<GetMyProfileBasicResponse> {
        val response = GetMyProfileBasicResponse(
            description = description,
            nickname = nickname,
            age = null,
            birthdate = birthDate,
            height = height,
            weight = weight,
            location = location,
            job = job,
            smokingStatus = smokingStatus,
            snsActivityLevel = snsActivityLevel,
            imageUrl = imageUrl,
            contacts = contacts.map { contact ->
                ContactResponse(
                    type = contact.type.name,
                    value = contact.content
                )
            }
        )

        return Result.success(response)
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

    override suspend fun disconnectSSE(): Result<Unit> {
        TODO("Not yet implemented")
    }

    fun setValuePicks(picks: List<ValuePickResponse>) {
        valuePicks = picks
    }

    fun setValueTalks(talks: List<ValueTalkResponse>) {
        valueTalks = talks
    }
}
