package com.puzzle.piece.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.puzzle.auth.navigation.AuthGraph
import com.puzzle.auth.navigation.authNavGraph
import com.puzzle.matching.navigation.navigateToMatchingDetailRoute
import com.puzzle.piece.navigation.home.homeNavGraph
import com.puzzle.piece.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = AuthGraph,
    ) {
        authNavGraph(
            onLoginSuccess = appState::navigateToHome,
        )
        homeNavGraph(
            onNavigateToDetail = navController::navigateToMatchingDetailRoute,
            onBack = navController::popBackStack,
            modifier = modifier,
        )
    }
}
