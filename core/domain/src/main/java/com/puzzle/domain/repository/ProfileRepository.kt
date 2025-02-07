package com.puzzle.domain.repository

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.domain.model.profile.ValueTalkAnswer

interface ProfileRepository {
    suspend fun loadValuePicks(): Result<Unit>
    suspend fun retrieveValuePick(): Result<List<ValuePick>>

    suspend fun loadValueTalks(): Result<Unit>
    suspend fun retrieveValueTalk(): Result<List<ValueTalk>>

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
        phoneNumber: String,
        smokingStatus: String,
        snsActivityLevel: String,
        contacts: List<Contact>,
        valuePicks: List<ValuePickAnswer>,
        valueTalks: List<ValueTalkAnswer>,
    ): Result<Unit>
}
