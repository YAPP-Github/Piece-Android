package com.puzzle.domain.usecase.matching

import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.repository.MatchingRepository
import javax.inject.Inject

class GetOpponentProfileUseCase @Inject constructor(
    private val matchingRepository: MatchingRepository,
) {
    suspend operator fun invoke(): Result<OpponentProfile> = matchingRepository.getOpponentProfile()
}
