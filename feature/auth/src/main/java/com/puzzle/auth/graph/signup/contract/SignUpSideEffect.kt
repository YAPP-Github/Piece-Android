package com.puzzle.auth.graph.signup.contract

import com.puzzle.navigation.NavigationEvent

sealed class SignUpSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : SignUpSideEffect()
}
