package com.yapp.chaeum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.auth.navigation.AuthGraph
import com.example.auth.navigation.AuthGraphDest
import com.example.auth.navigation.authNavGraph
import com.example.etc.navigation.etcScreen
import com.example.matching.navigation.MatchingGraph
import com.example.matching.navigation.MatchingGraphDest
import com.example.matching.navigation.matchingNavGraph
import com.example.mypage.navigation.myPageScreen
import com.yapp.chaeum.ui.AppState
import kotlinx.serialization.Serializable

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
            onLoginSuccess = {
                navController.navigate(HomeGraph) {
                    popUpTo(AuthGraphDest.AuthRoute) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )
        homeNavGraph(
            onNavigateToDetail = { navController.navigate(MatchingGraphDest.MatchingDetailRoute) },
            onBack = { navController.popBackStack() }
        )
    }
}

@Serializable
data object HomeGraph

//
//fun NavController.navigateToHome(navOptions: NavOptions) =
//    navigate(route = HomeGraphDest.MatchingGraph, navOptions)

fun NavGraphBuilder.homeNavGraph(
    onNavigateToDetail: () -> Unit,
    onBack: () -> Unit,
) {
    navigation<HomeGraph>(startDestination = MatchingGraph) {
        matchingNavGraph(
            onNavigateToDetail = onNavigateToDetail,
            onBack = onBack,
        )
        myPageScreen()
        etcScreen()
    }
}
