package com.puzzle.domain.spy.repository

import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.repository.MatchingRepository

class SpyMatchingRepository : MatchingRepository {
    private var localOpponentProfile: OpponentProfile? = null
    private var remoteOpponentProfile: OpponentProfile? = null
    private var shouldFailLocalRetrieval = false
    var loadOpponentProfileCallCount = 0
        private set

    fun setLocalOpponentProfile(profile: OpponentProfile?) {
        localOpponentProfile = profile
    }

    fun setRemoteOpponentProfile(profile: OpponentProfile) {
        remoteOpponentProfile = profile
    }

    fun setShouldFailLocalRetrieval(shouldFail: Boolean) {
        shouldFailLocalRetrieval = shouldFail
    }

    override suspend fun retrieveOpponentProfile(): Result<OpponentProfile> =
        if (shouldFailLocalRetrieval) {
            Result.failure(NoSuchElementException("No value present in DataStore"))
        } else {
            localOpponentProfile?.let { Result.success(it) }
                ?: Result.failure(NoSuchElementException("No value present in DataStore"))
        }

    override suspend fun loadOpponentProfile(): Result<Unit> {
        loadOpponentProfileCallCount++
        shouldFailLocalRetrieval = false
        localOpponentProfile = remoteOpponentProfile
        return Result.success(Unit)
    }

    override suspend fun reportUser(userId: Int, reason: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun blockUser(userId: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getMatchInfo(): Result<MatchInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun checkMatchingPiece(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptMatching(): Result<Unit> {
        TODO("Not yet implemented")
    }
}
