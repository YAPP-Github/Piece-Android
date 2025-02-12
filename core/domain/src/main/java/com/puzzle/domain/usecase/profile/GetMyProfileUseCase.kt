package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<MyProfile> =
        profileRepository.retrieveMyProfile()
            .recoverCatching {
                profileRepository.loadMyProfile().getOrThrow()
                profileRepository.retrieveMyProfile().getOrThrow()
            }
}
