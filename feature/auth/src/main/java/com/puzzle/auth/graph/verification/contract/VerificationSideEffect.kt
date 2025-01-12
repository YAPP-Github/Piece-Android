package com.puzzle.auth.graph.verification.contract

import com.puzzle.navigation.NavigationEvent

sealed class VerificationSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : VerificationSideEffect()
}
