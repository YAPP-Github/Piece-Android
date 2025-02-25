package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.datastore.datasource.matching.LocalMatchingDataSource
import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.model.profile.OpponentProfileBasic
import com.puzzle.domain.model.profile.OpponentValuePick
import com.puzzle.domain.model.profile.OpponentValueTalk
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse
import com.puzzle.network.model.profile.ContactResponse
import com.puzzle.network.source.matching.MatchingDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchingRepositoryImpl @Inject constructor(
    private val localMatchingDataSource: LocalMatchingDataSource,
    private val matchingDataSource: MatchingDataSource,
) : MatchingRepository {
    override suspend fun reportUser(userId: Int, reason: String): Result<Unit> =
        matchingDataSource.reportUser(userId = userId, reason = reason)

    override suspend fun refuseMatch(): Result<Unit> =
        matchingDataSource.refuseMatch()

    override suspend fun blockUser(userId: Int): Result<Unit> = matchingDataSource.blockUser(userId)

    override suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit> =
        matchingDataSource.blockContacts(phoneNumbers)

    override suspend fun getOpponentContacts(): Result<List<Contact>> =
        matchingDataSource.getContacts()
            .mapCatching { response ->
                response.contacts.orEmpty()
                    .map(ContactResponse::toDomain)
            }

    override suspend fun getMatchInfo(): Result<MatchInfo> =
        matchingDataSource.getMatchInfo().mapCatching(GetMatchInfoResponse::toDomain)

    override suspend fun retrieveOpponentProfile(): Result<OpponentProfile> {
        val profile = localMatchingDataSource.opponentProfile.first()
        return if (profile != null) {
            Result.success(profile)
        } else {
            Result.failure(NullPointerException("OpponentProfile is null"))
        }
    }

    override suspend fun loadOpponentProfile(): Result<Unit> = suspendRunCatching {
        coroutineScope {
            val valueTalksDeferred = async { getOpponentValueTalks() }
            val valuePicksDeferred = async { getOpponentValuePicks() }
            val profileBasicDeferred = async { getOpponentProfileBasic() }
            val profileImageDeferred = async { getOpponentProfileImage() }

            val valuePicks = valuePicksDeferred.await().getOrThrow()
            val valueTalks = valueTalksDeferred.await().getOrThrow()
            val profileBasic = profileBasicDeferred.await().getOrThrow()
            val imageUrl = profileImageDeferred.await().getOrThrow()

            val result = OpponentProfile(
                description = profileBasic.description,
                nickname = profileBasic.nickname,
                age = profileBasic.age,
                birthYear = profileBasic.birthYear,
                height = profileBasic.height,
                weight = profileBasic.weight,
                location = profileBasic.location,
                job = profileBasic.job,
                smokingStatus = profileBasic.smokingStatus,
                valuePicks = valuePicks,
                valueTalks = valueTalks,
                imageUrl = imageUrl,
            )
            localMatchingDataSource.setOpponentProfile(result)
        }
    }

    private suspend fun getOpponentValueTalks(): Result<List<OpponentValueTalk>> =
        matchingDataSource.getOpponentValueTalks()
            .mapCatching(GetOpponentValueTalksResponse::toDomain)

    private suspend fun getOpponentValuePicks(): Result<List<OpponentValuePick>> =
        matchingDataSource.getOpponentValuePicks()
            .mapCatching(GetOpponentValuePicksResponse::toDomain)

    private suspend fun getOpponentProfileBasic(): Result<OpponentProfileBasic> =
        matchingDataSource.getOpponentProfileBasic()
            .mapCatching(GetOpponentProfileBasicResponse::toDomain)

    private suspend fun getOpponentProfileImage(): Result<String> =
        matchingDataSource.getOpponentProfileImage()
            .mapCatching(GetOpponentProfileImageResponse::toDomain)

    override suspend fun checkMatchingPiece(): Result<Unit> =
        matchingDataSource.checkMatchingPiece()

    override suspend fun acceptMatching(): Result<Unit> = matchingDataSource.acceptMatching()
}
