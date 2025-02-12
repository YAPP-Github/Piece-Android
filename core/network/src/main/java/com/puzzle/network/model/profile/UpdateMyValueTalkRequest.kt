package com.puzzle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateMyValueTalkRequests(
    val profileValueTalkUpdateRequests: List<UpdateMyValueTalkRequest>,
)

@Serializable
data class UpdateMyValueTalkRequest(
    val profileValueTalkId: Int,
    val answer: String,
    val summary: String,
)
