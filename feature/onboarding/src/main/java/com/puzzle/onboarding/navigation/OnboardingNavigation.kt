package com.puzzle.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puzzle.navigation.OnboardingRoute
import com.puzzle.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingNavigation() {
    composable<OnboardingRoute> {
        OnboardingScreen()
    }
}
