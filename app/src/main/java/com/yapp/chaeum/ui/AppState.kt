package com.yapp.chaeum.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.auth.navigation.AuthGraphDest
import com.example.etc.navigation.navigateToEtc
import com.example.matching.navigation.navigateToMatchingGraph
import com.example.mypage.navigation.navigateToMyPage
import com.yapp.chaeum.navigation.TopLevelDestination
import com.yapp.chaeum.navigation.home.HomeGraph

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(navController) {
        AppState(navController = navController)
    }
}

class AppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigateToHome() {
        navController.navigate(HomeGraph) {
            popUpTo(AuthGraphDest.AuthRoute) {
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToTopLevelDestination(
        topLevelDestination: TopLevelDestination,
    ) {
        val topLevelNavOptions = navOptions {
            popUpTo(HomeGraph) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.MATCHING -> navController.navigateToMatchingGraph(topLevelNavOptions)
            TopLevelDestination.MYPAGE -> navController.navigateToMyPage(topLevelNavOptions)
            TopLevelDestination.ETC -> navController.navigateToEtc(topLevelNavOptions)
        }
    }
}