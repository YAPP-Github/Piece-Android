package com.puzzle.network.model.profile


import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val birthdate: String,
    val contacts: Map<String, String>,
    val description: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val job: String,
    val location: String,
    val nickname: String,
    val smokingStatus: String,
    val snsActivityLevel: String,
    val valuePicks: List<ValuePickAnswerRequest?>,
    val valueTalks: List<ValueTalkAnswerRequest?>,
)
