package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.data.image.ImageResizer
import com.puzzle.datastore.datasource.profile.LocalProfileDataSource
import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.profile.AiSummary
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.profile.ValueTalkQuestion
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.network.api.sse.SseClient
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.profile.SseAiSummaryResponse
import com.puzzle.network.source.profile.ProfileDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val imageResizer: ImageResizer,
    private val profileDataSource: ProfileDataSource,
    private val localProfileDataSource: LocalProfileDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val sseClient: SseClient,
) : ProfileRepository {
    override val aiSummary: Flow<AiSummary> =
        sseClient.aiSummaryResponse.map(SseAiSummaryResponse::toDomain)

    override suspend fun loadValuePickQuestions(): Result<Unit> = suspendRunCatching {
        val valuePickQuestions = profileDataSource.loadValuePickQuestions()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        localProfileDataSource.setValuePickQuestions(valuePickQuestions)
    }

    override suspend fun loadValueTalkQuestions(): Result<Unit> = suspendRunCatching {
        val valueTalkQuestions = profileDataSource.loadValueTalkQuestions()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        localProfileDataSource.setValueTalkQuestions(valueTalkQuestions)
    }

    override suspend fun retrieveValuePickQuestion(): Result<List<ValuePickQuestion>> =
        suspendRunCatching { localProfileDataSource.valuePickQuestions.first() }

    override suspend fun retrieveValueTalkQuestion(): Result<List<ValueTalkQuestion>> =
        suspendRunCatching { localProfileDataSource.valueTalkQuestions.first() }

    override suspend fun retrieveMyProfileBasic(): Result<MyProfileBasic> = suspendRunCatching {
        localProfileDataSource.myProfileBasic.first()
    }

    override suspend fun loadMyValueTalks(): Result<Unit> = suspendRunCatching {
        val valueTalks = profileDataSource.getMyValueTalks().getOrThrow().toDomain()

        localProfileDataSource.setMyValueTalks(valueTalks)
    }

    override suspend fun retrieveMyValueTalks(): Result<List<MyValueTalk>> = suspendRunCatching {
        localProfileDataSource.myValueTalks.first()
    }

    override suspend fun loadMyValuePicks(): Result<Unit> = suspendRunCatching {
        val valuePicks = profileDataSource.getMyValuePicks().getOrThrow().toDomain()

        localProfileDataSource.setMyValuePicks(valuePicks)
    }

    override suspend fun retrieveMyValuePicks(): Result<List<MyValuePick>> =
        suspendRunCatching {
            localProfileDataSource.myValuePicks.first()
        }

    override suspend fun loadMyProfileBasic(): Result<Unit> = suspendRunCatching {
        val profileBasic = profileDataSource.getMyProfileBasic().getOrThrow().toDomain()

        localProfileDataSource.setMyProfileBasic(profileBasic)
    }

    override suspend fun updateMyValueTalks(valueTalks: List<MyValueTalk>): Result<List<MyValueTalk>> =
        suspendRunCatching {
            val updatedValueTalks =
                profileDataSource.updateMyValueTalks(valueTalks).getOrThrow().toDomain()

            localProfileDataSource.setMyValueTalks(updatedValueTalks)
            updatedValueTalks
        }

    override suspend fun updateMyValuePicks(valuePicks: List<MyValuePick>): Result<List<MyValuePick>> =
        suspendRunCatching {
            val updatedValuePicks =
                profileDataSource.updateMyValuePicks(valuePicks).getOrThrow().toDomain()

            localProfileDataSource.setMyValuePicks(updatedValuePicks)
            updatedValuePicks
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
    ): Result<MyProfileBasic> = suspendRunCatching {
        val updatedProfileBasic = profileDataSource.updateMyProfileBasic(
            description = description,
            nickname = nickname,
            birthDate = birthDate,
            height = height,
            weight = weight,
            location = location,
            job = job,
            smokingStatus = smokingStatus,
            snsActivityLevel = snsActivityLevel,
            imageUrl = imageUrl,
            contacts = contacts
        ).getOrThrow().toDomain()

        localProfileDataSource.setMyProfileBasic(updatedProfileBasic)
        updatedProfileBasic
    }

    override suspend fun updateAiSummary(profileTalkId: Int, summary: String): Result<Unit> =
        suspendRunCatching {
            profileDataSource.updateAiSummary(
                profileTalkId = profileTalkId,
                summary = summary,
            ).onSuccess {
                val oldValueTalks = retrieveMyValueTalks().getOrThrow()
                val newValueTalks = oldValueTalks.map { valueTalk ->
                    if (valueTalk.id == profileTalkId) valueTalk.copy(summary = summary)
                    else valueTalk
                }

                localProfileDataSource.setMyValueTalks(newValueTalks)
            }
        }

    override suspend fun checkNickname(nickname: String): Result<Boolean> =
        profileDataSource.checkNickname(nickname)

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
    ): Result<Unit> = suspendRunCatching {
        val uploadedImageUrl = imageResizer.resizeImage(imageUrl).use { imageInputStream ->
            profileDataSource.uploadProfileImage(imageInputStream).getOrThrow()
        }

        val response = profileDataSource.uploadProfile(
            birthdate = birthdate,
            description = description,
            height = height,
            weight = weight,
            imageUrl = uploadedImageUrl,
            job = job,
            location = location,
            nickname = nickname,
            smokingStatus = smokingStatus,
            snsActivityLevel = snsActivityLevel,
            contacts = contacts,
            valuePicks = valuePicks,
            valueTalks = valueTalks
        ).getOrThrow()

        coroutineScope {
            val accessTokenJob = launch {
                response.accessToken?.let { localTokenDataSource.setAccessToken(it) }
            }
            val refreshTokenJob = launch {
                response.refreshToken?.let { localTokenDataSource.setRefreshToken(it) }
            }
            val userRoleJob = launch {
                response.role?.let { localUserDataSource.setUserRole(it) }
            }

            accessTokenJob.join()
            refreshTokenJob.join()
            userRoleJob.join()
        }
    }

    override suspend fun connectSSE(): Result<Unit> = suspendRunCatching { sseClient.connect() }
    override suspend fun disconnectSSE(): Result<Unit> = suspendRunCatching {
        profileDataSource.disconnectSSE().getOrThrow()
        sseClient.disconnect()
    }
}
