package com.puzzle.onboarding.contract

import com.puzzle.navigation.NavigationEvent

sealed class OnboardingIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : OnboardingIntent()
}