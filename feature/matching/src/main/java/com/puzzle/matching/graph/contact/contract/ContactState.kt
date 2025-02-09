package com.puzzle.matching.graph.contact.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Contact

data class ContactState(
    val isLoading: Boolean = false,
    val nickName: String = "",
    val contacts: List<Contact> = emptyList(),
    val selectedContact: Contact? = contacts.firstOrNull()
) : MavericksState