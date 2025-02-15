package com.puzzle.domain.spy.repository

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
import kotlinx.coroutines.flow.Flow

class SpyProfileRepository : ProfileRepository {
    private var localMyProfileBasic: MyProfileBasic? = null
    private var localMyValueTalks: List<MyValueTalk>? = null
    private var localMyValuePicks: List<MyValuePick>? = null

    private var remoteMyProfileBasic: MyProfileBasic? = null
    private var remoteMyValueTalks: List<MyValueTalk>? = null
    private var remoteMyValuePicks: List<MyValuePick>? = null

    var loadMyProfileBasicCallCount = 0
        private set

    var loadMyValueTalksCallCount = 0
        private set

    var loadMyValuePicksCallCount = 0
        private set

    fun setLocalMyProfileBasic(profileBasic: MyProfileBasic) {
        localMyProfileBasic = profileBasic
    }

    fun setLocalMyValueTalks(valueTalks: List<MyValueTalk>) {
        localMyValueTalks = valueTalks
    }

    fun setLocalMyValuePicks(valuePicks: List<MyValuePick>) {
        localMyValuePicks = valuePicks
    }

    fun setRemoteMyProfileBasic(profileBasic: MyProfileBasic) {
        remoteMyProfileBasic = profileBasic
    }

    fun setRemoteMyValueTalks(valueTalks: List<MyValueTalk>) {
        remoteMyValueTalks = valueTalks
    }

    fun setRemoteMyValuePicks(valuePicks: List<MyValuePick>) {
        remoteMyValuePicks = valuePicks
    }

    override val aiSummary: Flow<AiSummary>
        get() = TODO("Not yet implemented")

    override suspend fun loadValuePickQuestions(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun retrieveValuePickQuestion(): Result<List<ValuePickQuestion>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadValueTalkQuestions(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun retrieveValueTalkQuestion(): Result<List<ValueTalkQuestion>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadMyProfileBasic(): Result<Unit> {
        loadMyProfileBasicCallCount++
        localMyProfileBasic = remoteMyProfileBasic
        return Result.success(Unit)
    }

    override suspend fun retrieveMyProfileBasic(): Result<MyProfileBasic> =
        localMyProfileBasic?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No value present in DataStore"))


    override suspend fun loadMyValueTalks(): Result<Unit> {
        loadMyValueTalksCallCount++
        localMyValueTalks = remoteMyValueTalks
        return Result.success(Unit)
    }

    override suspend fun retrieveMyValueTalks(): Result<List<MyValueTalk>> =
        localMyValueTalks?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No value present in DataStore"))


    override suspend fun loadMyValuePicks(): Result<Unit> {
        loadMyValuePicksCallCount++
        localMyValuePicks = remoteMyValuePicks
        return Result.success(Unit)
    }

    override suspend fun retrieveMyValuePicks(): Result<List<MyValuePick>> =
        localMyValuePicks?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No value present in DataStore"))


    override suspend fun updateMyValueTalks(valueTalks: List<MyValueTalk>): Result<List<MyValueTalk>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMyValuePicks(valuePicks: List<MyValuePick>): Result<List<MyValuePick>> {
        TODO("Not yet implemented")
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
    ): Result<MyProfileBasic> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAiSummary(profileTalkId: Int, summary: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkNickname(nickname: String): Result<Boolean> {
        TODO("Not yet implemented")
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
        smokingStatus: String,
        snsActivityLevel: String,
        contacts: List<Contact>,
        valuePicks: List<ValuePickAnswer>,
        valueTalks: List<ValueTalkAnswer>
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun connectSSE(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun disconnectSSE(): Result<Unit> {
        TODO("Not yet implemented")
    }
}
