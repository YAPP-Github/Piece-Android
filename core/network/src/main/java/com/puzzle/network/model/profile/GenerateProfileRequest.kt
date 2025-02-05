package com.puzzle.network.model.profile


import kotlinx.serialization.Serializable

@Serializable
data class GenerateProfileRequest(
    val birthdate: String,
    val contacts: Map<String, String>,
    val description: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val job: String,
    val location: String,
    val nickname: String,
    val phoneNumber: String,
    val smokingStatus: String,
    val snsActivityLevel: String,
    val valuePicks: List<ValuePickAnswerRequest?>,
    val valueTalks: List<ValueTalkAnswerRequest?>,
)

@Serializable
data class ValuePickAnswerRequest(
    val valuePickId: Int,
    val selectedAnswer: Int,
)

@Serializable
data class ValueTalkAnswerRequest(
    val valueTalkId: Int,
    val answer: String,
)
