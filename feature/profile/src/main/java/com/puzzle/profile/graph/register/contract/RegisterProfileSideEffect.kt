package com.puzzle.profile.graph.register.contract

import com.puzzle.navigation.NavigationEvent

sealed class RegisterProfileSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : RegisterProfileSideEffect()
}
