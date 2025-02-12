package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.data.image.ImageResizer
import com.puzzle.datastore.datasource.profile.LocalProfileDataSource
import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.profile.ValueTalkQuestion
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.profile.GetMyProfileBasicResponse
import com.puzzle.network.model.profile.GetMyValuePicksResponse
import com.puzzle.network.model.profile.GetMyValueTalksResponse
import com.puzzle.network.source.profile.ProfileDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val imageResizer: ImageResizer,
    private val profileDataSource: ProfileDataSource,
    private val localProfileDataSource: LocalProfileDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : ProfileRepository {
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

    override suspend fun retrieveMyProfile(): Result<MyProfile> =
        suspendRunCatching { localProfileDataSource.myProfile.first() }

    override suspend fun loadMyProfile(): Result<Unit> = suspendRunCatching {
        coroutineScope {
            val valueTalksDeferred = async { getMyValueTalks() }
            val valuePicksDeferred = async { getMyValuePicks() }
            val profileBasicDeferred = async { getMyProfileBasic() }

            val valuePicks = valuePicksDeferred.await().getOrThrow()
            val valueTalks = valueTalksDeferred.await().getOrThrow()
            val profileBasic = profileBasicDeferred.await().getOrThrow()

            val result = MyProfile(
                description = profileBasic.description,
                nickname = profileBasic.nickname,
                age = profileBasic.age,
                birthDate = profileBasic.birthDate,
                height = profileBasic.height,
                weight = profileBasic.weight,
                location = profileBasic.location,
                job = profileBasic.job,
                smokingStatus = profileBasic.smokingStatus,
                contacts = profileBasic.contacts,
                imageUrl = profileBasic.imageUrl,
                valuePicks = valuePicks,
                valueTalks = valueTalks,
            )

            localProfileDataSource.setMyProfile(result)
        }
    }

    private suspend fun getMyValueTalks(): Result<List<MyValueTalk>> =
        profileDataSource.getMyValueTalks()
            .mapCatching(GetMyValueTalksResponse::toDomain)

    private suspend fun getMyValuePicks(): Result<List<MyValuePick>> =
        profileDataSource.getMyValuePicks()
            .mapCatching(GetMyValuePicksResponse::toDomain)

    private suspend fun getMyProfileBasic(): Result<MyProfileBasic> =
        profileDataSource.getMyProfileBasic()
            .mapCatching(GetMyProfileBasicResponse::toDomain)

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
        val uploadedImageUrl =
            imageResizer.resizeImage(imageUrl).use { imageInputStream ->
                profileDataSource.uploadProfileImage(imageInputStream)
                    .getOrThrow()
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
}
