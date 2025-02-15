package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateMyValuePickRequests(
    val profileValuePickUpdateRequests: List<UpdateMyValuePickRequest>,
)

@Serializable
data class UpdateMyValuePickRequest(
    val profileValuePickId: Int,
    val selectedAnswer: Int,
)
