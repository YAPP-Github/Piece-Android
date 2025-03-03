package com.puzzle.onboarding.contract

sealed class OnboardingIntent {
    data object OnStartClick : OnboardingIntent()
}
