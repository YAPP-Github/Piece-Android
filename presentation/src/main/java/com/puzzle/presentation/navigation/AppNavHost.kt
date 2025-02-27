package com.puzzle.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.puzzle.auth.navigation.authNavGraph
import com.puzzle.common.ui.ANIMATION_DURATION
import com.puzzle.matching.navigation.matchingNavGraph
import com.puzzle.navigation.AuthGraph
import com.puzzle.onboarding.navigation.onboardingNavigation
import com.puzzle.profile.navigation.profileNavGraph
import com.puzzle.setting.navigation.settingNavGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        popExitTransition = { fadeOut(tween(ANIMATION_DURATION)) },
        popEnterTransition = { fadeIn(tween(ANIMATION_DURATION)) },
        startDestination = AuthGraph,
        modifier = modifier,
    ) {
        onboardingNavigation()
        authNavGraph()
        matchingNavGraph()
        profileNavGraph()
        settingNavGraph()
    }
}
