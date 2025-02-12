package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyValuePicksUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<List<MyValuePick>> =
        profileRepository.retrieveMyValuePicks()
            .recoverCatching {
                profileRepository.loadMyValuePicks().getOrThrow()
                profileRepository.retrieveMyValuePicks().getOrThrow()
            }
}
