package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyValuePicksUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<List<MyValuePick>> {
        val result = profileRepository.retrieveMyValuePicks()
        return if (result.isSuccess) {
            result
        } else {
            // 실패 시 원격 로드 후 재조회
            profileRepository.loadMyValuePicks().getOrThrow()
            profileRepository.retrieveMyValuePicks()
        }
    }
}
