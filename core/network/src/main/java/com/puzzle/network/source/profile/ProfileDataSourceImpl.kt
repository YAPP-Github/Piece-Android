package com.puzzle.network.source.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.profile.UploadProfileRequest
import com.puzzle.network.model.profile.UploadProfileResponse
import com.puzzle.network.model.profile.ValuePickAnswerRequest
import com.puzzle.network.model.profile.ValueTalkAnswerRequest
import com.puzzle.network.model.unwrapData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileDataSourceImpl @Inject constructor(
    private val pieceApi: PieceApi,
) : ProfileDataSource {
    override suspend fun loadValuePicks(): Result<LoadValuePicksResponse> =
        pieceApi.loadValuePicks().unwrapData()

    override suspend fun loadValueTalks(): Result<LoadValueTalksResponse> =
        pieceApi.loadValueTalks().unwrapData()

    override suspend fun checkNickname(nickname: String): Result<Boolean> =
        pieceApi.checkNickname(nickname).unwrapData()

    override suspend fun uploadProfileImage(imageInputStream: InputStream): Result<String> {
        val requestImage = MultipartBody.Part.createFormData(
            name = "file",
            filename = "profile_${UUID.randomUUID()}.webp",
            body = imageInputStream.readBytes().toRequestBody("image/webp".toMediaTypeOrNull())
        )

        return pieceApi.uploadProfileImage(requestImage).unwrapData()
    }

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
    ): Result<UploadProfileResponse> = pieceApi.uploadProfile(
        UploadProfileRequest(
            birthdate = birthdate,
            description = description,
            height = height,
            weight = weight,
            imageUrl = imageUrl,
            job = job,
            location = location,
            nickname = nickname,
            phoneNumber = phoneNumber,
            smokingStatus = smokingStatus,
            snsActivityLevel = snsActivityLevel,
            valuePicks = valuePicks.map {
                ValuePickAnswerRequest(
                    valuePickId = it.valuePickId,
                    selectedAnswer = it.selectedAnswer
                )
            },
            valueTalks = valueTalks.map {
                ValueTalkAnswerRequest(
                    valueTalkId = it.valueTalkId,
                    answer = it.answer,
                )
            },
            contacts = contacts.associate { it.snsPlatform.name to it.content }
        )
    ).unwrapData()
}
