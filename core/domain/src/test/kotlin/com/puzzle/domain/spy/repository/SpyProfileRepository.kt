package com.puzzle.domain.spy.repository

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.profile.ValueTalkQuestion
import com.puzzle.domain.repository.ProfileRepository

class SpyProfileRepository : ProfileRepository {
    private var localMyProfile: MyProfile? = null
    private var remoteMyProfile: MyProfile? = null
    private var shouldFailLocalRetrieval = false
    var loadMyProfileCallCount = 0
        private set

    fun setLocalMyProfile(profile: MyProfile?) {
        localMyProfile = profile
    }

    fun setRemoteOpponentProfile(profile: MyProfile) {
        remoteMyProfile = profile
    }

    fun setShouldFailLocalRetrieval(shouldFail: Boolean) {
        shouldFailLocalRetrieval = shouldFail
    }

    override suspend fun loadValuePickQuestions(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveValuePickQuestion(): Result<List<ValuePickQuestion>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadValueTalkQuestions(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveValueTalkQuestion(): Result<List<ValueTalkQuestion>> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveMyProfile(): Result<MyProfile> =
        if (shouldFailLocalRetrieval) {
            Result.failure(NoSuchElementException("No value present in DataStore"))
        } else {
            localMyProfile?.let { Result.success(it) }
                ?: Result.failure(NoSuchElementException("No value present in DataStore"))
        }

    override suspend fun loadMyProfile(): Result<Unit> {
        loadMyProfileCallCount++
        shouldFailLocalRetrieval = false
        localMyProfile = remoteMyProfile
        return Result.success(Unit)
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

}
