package com.puzzle.domain.repository

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.profile.ValueTalkQuestion

interface ProfileRepository {
    suspend fun loadValuePickQuestions(): Result<Unit>
    suspend fun retrieveValuePickQuestion(): Result<List<ValuePickQuestion>>

    suspend fun loadValueTalkQuestions(): Result<Unit>
    suspend fun retrieveValueTalkQuestion(): Result<List<ValueTalkQuestion>>

    suspend fun retrieveMyProfile(): Result<MyProfile>
    suspend fun loadMyProfile(): Result<Unit>

    suspend fun checkNickname(nickname: String): Result<Boolean>
    suspend fun uploadProfile(
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
    ): Result<Unit>
}
