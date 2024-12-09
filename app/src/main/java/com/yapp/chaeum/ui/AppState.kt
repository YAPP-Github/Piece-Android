package com.yapp.chaeum.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.auth.navigation.AuthRoute
import com.example.auth.navigation.navigateToAuth
import com.example.etc.navigation.navigateToEtc
import com.example.matching.navigation.MatchingRoute
import com.example.matching.navigation.navigateToMatching
import com.example.mypage.navigation.navigateToMyPage
import com.yapp.chaeum.navigation.TopLevelDestination

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController,
    ) {
        AppState(
            navController = navController,
        )
    }
}

class AppState(
    val navController: NavHostController,
) {
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.MATCHING -> navController.navigateToMatching(topLevelNavOptions)
            TopLevelDestination.MYPAGE -> navController.navigateToMyPage(topLevelNavOptions)
            TopLevelDestination.ETC -> navController.navigateToEtc(topLevelNavOptions)
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun logout() {
        navController.navigateToAuth(
            navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        )
    }

    fun loginSuccess() {
        navController.navigate(MatchingRoute) {
            popUpTo(AuthRoute) { inclusive = true }
            launchSingleTop = true
        }
    }
}