package com.puzzle.matching.graph.contact.contract

import com.puzzle.navigation.NavigationEvent

sealed class ContactSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : ContactSideEffect()
}