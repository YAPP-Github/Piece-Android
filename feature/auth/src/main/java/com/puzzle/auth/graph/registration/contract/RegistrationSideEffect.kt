package com.puzzle.auth.graph.registration.contract

import com.puzzle.navigation.NavigationEvent

sealed class RegistrationSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : RegistrationSideEffect()
}
