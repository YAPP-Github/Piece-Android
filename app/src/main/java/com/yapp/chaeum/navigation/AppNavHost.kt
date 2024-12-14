package com.yapp.chaeum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.auth.navigation.AuthGraph
import com.example.auth.navigation.authNavGraph
import com.example.matching.navigation.navigateToMatchingDetailRoute
import com.yapp.chaeum.navigation.home.homeNavGraph
import com.yapp.chaeum.ui.AppState

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
