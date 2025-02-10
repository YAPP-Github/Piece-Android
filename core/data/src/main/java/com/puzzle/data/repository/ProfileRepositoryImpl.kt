package com.puzzle.data.repository

import android.util.Log
import com.puzzle.common.suspendRunCatching
import com.puzzle.data.image.ImageResizer
import com.puzzle.database.model.matching.ValuePickAnswerEntity
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValuePickQuestionEntity
import com.puzzle.database.model.matching.ValueTalkEntity
import com.puzzle.database.source.profile.LocalProfileDataSource
import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.profile.ValueTalkQuestion
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.source.profile.ProfileDataSource
import kotlinx.coroutines.coroutineScope
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
        val valuePicks = profileDataSource.loadValuePickQuestions()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valuePickEntities = valuePicks.map { valuePick ->
            ValuePickEntity(
                valuePickQuestion = ValuePickQuestionEntity(
                    id = valuePick.id,
                    category = valuePick.category,
                    question = valuePick.question,
                ),
                answers = valuePick.answers.map { answer ->
                    ValuePickAnswerEntity(
                        questionsId = valuePick.id,
                        number = answer.number,
                        content = answer.content,
                    )
                }
            )
        }

        localProfileDataSource.replaceValuePickQuestions(valuePickEntities)
    }

    override suspend fun loadValueTalkQuestions(): Result<Unit> = suspendRunCatching {
        val valueTalks = profileDataSource.loadValueTalkQuestions()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valueTalkEntities = valueTalks.map {
            ValueTalkEntity(
                id = it.id,
                title = it.title,
                category = it.category,
                helpMessages = it.guides,
            )
        }

        localProfileDataSource.replaceValueTalkQuestions(valueTalkEntities)
    }

    override suspend fun retrieveValuePickQuestion(): Result<List<ValuePickQuestion>> =
        suspendRunCatching {
            localProfileDataSource.retrieveValuePickQuestions()
                .map(ValuePickEntity::toDomain)
        }

    override suspend fun retrieveValueTalkQuestion(): Result<List<ValueTalkQuestion>> =
        suspendRunCatching {
            localProfileDataSource.retrieveValueTalkQuestions()
                .map(ValueTalkEntity::toDomain)
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
        valuePicks: List<com.puzzle.domain.model.profile.ValuePickAnswer>,
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
