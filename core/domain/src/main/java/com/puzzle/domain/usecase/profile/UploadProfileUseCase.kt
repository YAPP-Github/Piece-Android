package com.puzzle.domain.usecase.profile

import com.puzzle.common.suspendRunCatching
import com.puzzle.common.toBirthDate
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        birthdate: String,
        description: String,
        height: Int,
        weight: Int,
        imageUrl: String,
        job: String,
        location: String,
        nickname: String,
        smokingStatus: String,
        snsActivityLevel: String,
        contacts: List<Contact>,
        valuePicks: List<ValuePickAnswer>,
        valueTalks: List<ValueTalkAnswer>,
    ): Result<Unit> = suspendRunCatching {
        profileRepository.uploadProfile(
            birthdate = birthdate.toBirthDate(),
            description = description,
            height = height,
            weight = weight,
            imageUrl = imageUrl,
            job = job,
            location = location,
            nickname = nickname,
            smokingStatus = smokingStatus,
            snsActivityLevel = snsActivityLevel,
            contacts = contacts,
            valuePicks = valuePicks,
            valueTalks = valueTalks
        ).getOrThrow()
    }
}
