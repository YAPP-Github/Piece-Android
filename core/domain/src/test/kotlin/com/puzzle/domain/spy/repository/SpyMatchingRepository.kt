package com.puzzle.domain.spy.repository

import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.repository.MatchingRepository

class SpyMatchingRepository : MatchingRepository {
    private var localOpponentProfile: OpponentProfile? = null
    private var remoteOpponentProfile: OpponentProfile? = null
    var loadOpponentProfileCallCount = 0
        private set

    fun setLocalOpponentProfile(profile: OpponentProfile?) {
        localOpponentProfile = profile
    }

    fun setRemoteOpponentProfile(profile: OpponentProfile) {
        remoteOpponentProfile = profile
    }

    override suspend fun retrieveOpponentProfile(): Result<OpponentProfile> =
        localOpponentProfile?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No value present in DataStore"))


    override suspend fun loadOpponentProfile(): Result<Unit> {
        loadOpponentProfileCallCount++
        localOpponentProfile = remoteOpponentProfile
        return Result.success(Unit)
    }

    override suspend fun refuseMatch(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun reportUser(userId: Int, reason: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getOpponentContacts(): Result<List<Contact>> {
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
