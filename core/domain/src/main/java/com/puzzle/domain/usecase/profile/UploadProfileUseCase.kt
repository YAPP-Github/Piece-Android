package com.puzzle.domain.usecase.profile

import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
}
