package com.yapp.chaeum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auth.navigation.AuthGraph
import com.example.auth.navigation.authNavGraph
import com.yapp.chaeum.ui.AppState
import com.yapp.chaeum.ui.HomeGraph
import com.yapp.chaeum.ui.HomeRoute

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = AuthGraph,
        modifier = modifier,
    ) {
        authNavGraph(
            onLoginSuccess = { appState.loginSuccess() },
        )
        composable<HomeGraph> {
            HomeRoute()
        }
    }
}
