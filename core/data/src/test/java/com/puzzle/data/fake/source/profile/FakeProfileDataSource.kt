package com.puzzle.data.fake.source.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.matching.ValuePickResponse
import com.puzzle.network.model.matching.ValueTalkResponse
import com.puzzle.network.model.profile.UploadProfileResponse
import com.puzzle.network.source.profile.ProfileDataSource

class FakeProfileDataSource : ProfileDataSource {
    private var valuePicks = listOf<ValuePickResponse>()
    private var valueTalks = listOf<ValueTalkResponse>()

    override suspend fun loadValuePicks(): Result<LoadValuePicksResponse> =
        Result.success(LoadValuePicksResponse(responses = valuePicks))

    override suspend fun loadValueTalks(): Result<LoadValueTalksResponse> =
        Result.success(LoadValueTalksResponse(responses = valueTalks))

    override suspend fun checkNickname(nickname: String): Result<Boolean> =
        Result.success(true)

    override suspend fun uploadProfileImage(file: String): Result<String> = Result.success("")

    override suspend fun uploadProfile(
        birthdate: String,
        description: String,
        height: Int,
        weight: Int,
        imageUrl: String,
        job: String,
        location: String,
        nickname: String,
        phoneNumber: String,
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
        valuePicks = picks
    }

    fun setValueTalks(talks: List<ValueTalkResponse>) {
        valueTalks = talks
    }
}
