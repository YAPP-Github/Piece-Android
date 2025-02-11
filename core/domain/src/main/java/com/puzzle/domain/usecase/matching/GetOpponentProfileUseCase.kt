package com.puzzle.domain.usecase.matching

import com.puzzle.common.suspendRunCatching
import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.repository.MatchingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetOpponentProfileUseCase @Inject constructor(
    private val matchingRepository: MatchingRepository,
) {
    suspend operator fun invoke(): Result<OpponentProfile> = suspendRunCatching {
        coroutineScope {
            val valueTalksDeferred = async { matchingRepository.getOpponentValueTalks() }
            val valuePicksDeferred = async { matchingRepository.getOpponentValuePicks() }
            val profileBasicDeferred = async { matchingRepository.getOpponentProfileBasic() }
            val profileImageDeferred = async { matchingRepository.getOpponentProfileImage() }

            val valuePicks = valuePicksDeferred.await().getOrThrow()
            val valueTalks = valueTalksDeferred.await().getOrThrow()
            val profileBasic = profileBasicDeferred.await().getOrThrow()
            val imageUrl = profileImageDeferred.await().getOrThrow()

            OpponentProfile(
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
        }
    }
}
