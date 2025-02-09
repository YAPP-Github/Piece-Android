package com.puzzle.network.model.matching

import kotlinx.serialization.Serializable

@Serializable
data class BlockContactsRequest(
    val phoneNumbers: List<String>,
)
