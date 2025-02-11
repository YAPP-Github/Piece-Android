package com.puzzle.matching.graph.contact.contract

import com.puzzle.domain.model.profile.Contact

sealed class ContactIntent {
    data class OnContactClick(val selectedContact: Contact) : ContactIntent()
    data object OnCloseClick : ContactIntent()
}