package com.puzzle.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.puzzle.auth.navigation.authNavGraph
import com.puzzle.matching.navigation.matchingNavGraph
import com.puzzle.navigation.AuthGraph
import com.puzzle.profile.navigation.profileNavGraph
import com.puzzle.setting.navigation.settingScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        startDestination = AuthGraph,
        modifier = modifier,
    ) {
        authNavGraph()
        matchingNavGraph()
        profileNavGraph()
        settingScreen()
    }
}
