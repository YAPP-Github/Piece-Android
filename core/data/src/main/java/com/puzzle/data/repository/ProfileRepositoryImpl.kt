package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.database.model.matching.ValuePickAnswer
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValuePickQuestion
import com.puzzle.database.model.matching.ValueTalkEntity
import com.puzzle.database.source.profile.LocalProfileDataSource
import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.source.profile.ProfileDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val localProfileDataSource: LocalProfileDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : ProfileRepository {
    override suspend fun loadValuePicks(): Result<Unit> = suspendRunCatching {
        val valuePicks = profileDataSource.loadValuePicks()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valuePickEntities = valuePicks.map { valuePick ->
            ValuePickEntity(
                valuePickQuestion = ValuePickQuestion(
                    id = valuePick.id,
                    category = valuePick.category,
                    question = valuePick.question,
                ),
                answers = valuePick.answers.map { answer ->
                    ValuePickAnswer(
                        questionsId = valuePick.id,
                        number = answer.number,
                        content = answer.content,
                    )
                }
            )
        }

        localProfileDataSource.replaceValuePicks(valuePickEntities)
    }

    override suspend fun loadValueTalks(): Result<Unit> = suspendRunCatching {
        val valueTalks = profileDataSource.loadValueTalks()
            .getOrThrow()
            .toDomain()
            .filter { it.id != UNKNOWN_INT }

        val valueTalkEntities = valueTalks.map {
            ValueTalkEntity(
                id = it.id,
                title = it.title,
                category = it.category,
                helpMessages = it.helpMessages,
            )
        }

        localProfileDataSource.replaceValueTalks(valueTalkEntities)
    }

    override suspend fun retrieveValuePick(): Result<List<ValuePick>> = suspendRunCatching {
        localProfileDataSource.retrieveValuePicks()
            .map(ValuePickEntity::toDomain)
    }

    override suspend fun retrieveValueTalk(): Result<List<ValueTalk>> = suspendRunCatching {
        localProfileDataSource.retrieveValueTalks()
            .map(ValueTalkEntity::toDomain)
    }

    override suspend fun checkNickname(nickname: String): Result<Boolean> =
        profileDataSource.checkNickname(nickname)

    override suspend fun generateProfile(
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
        valuePicks: List<com.puzzle.domain.model.profile.ValuePickAnswer>,
        valueTalks: List<ValueTalkAnswer>
    ): Result<Unit> = suspendRunCatching {
        val response = profileDataSource.generateProfile(
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
