package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyProfileBasicUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<MyProfileBasic> =
        profileRepository.retrieveMyProfileBasic()
            .recoverCatching {
                profileRepository.loadMyProfileBasic().getOrThrow()
                profileRepository.retrieveMyProfileBasic().getOrThrow()
            }
}
