package com.puzzle.onboarding.contract

import com.puzzle.navigation.NavigationEvent

sealed class OnboardingSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : OnboardingSideEffect()
}
