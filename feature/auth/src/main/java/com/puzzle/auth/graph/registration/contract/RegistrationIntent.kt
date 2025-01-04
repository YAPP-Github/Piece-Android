package com.puzzle.auth.graph.registration.contract

import com.puzzle.navigation.NavigationEvent

sealed class RegistrationIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : RegistrationIntent()
}