package com.puzzle.network.source.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.profile.UploadProfileResponse
import java.io.InputStream

interface ProfileDataSource {
    suspend fun loadValuePickQuestions(): Result<LoadValuePicksResponse>
    suspend fun loadValueTalkQuestions(): Result<LoadValueTalksResponse>
    suspend fun checkNickname(nickname: String): Result<Boolean>
    suspend fun uploadProfileImage(imageInputStream: InputStream): Result<String>
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
    ): Result<UploadProfileResponse>
}
