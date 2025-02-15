package com.puzzle.network.model.matching

import com.puzzle.network.model.profile.ContactResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetContactsResponse(
    val contacts: List<ContactResponse>? = null,
)
