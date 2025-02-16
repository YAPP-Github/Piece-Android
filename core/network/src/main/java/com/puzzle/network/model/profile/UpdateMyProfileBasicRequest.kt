package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateMyProfileBasicRequest(
    val description: String,
    val nickname: String,
    val birthdate: String,
    val height: Int,
    val weight: Int,
    val location: String,
    val job: String,
    val smokingStatus: String,
    val snsActivityLevel: String,
    val imageUrl: String,
    val contacts: Map<String, String>,
)
