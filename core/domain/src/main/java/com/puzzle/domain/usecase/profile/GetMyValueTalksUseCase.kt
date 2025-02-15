package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyValueTalksUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<List<MyValueTalk>> {
        val result = profileRepository.retrieveMyValueTalks()
        return if (result.isSuccess) {
            result
        } else {
            profileRepository.loadMyValueTalks().getOrThrow()
            profileRepository.retrieveMyValueTalks()
        }
    }
}
