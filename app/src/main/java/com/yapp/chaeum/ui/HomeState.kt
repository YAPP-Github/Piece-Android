package com.yapp.chaeum.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.etc.navigation.navigateToEtc
import com.example.matching.navigation.navigateToMatching
import com.example.mypage.navigation.navigateToMyPage
import com.yapp.chaeum.navigation.TopLevelDestination

@Composable
fun rememberHomeState(
    navController: NavHostController = rememberNavController(),
): HomeState {
    return remember(navController) {
        HomeState(navController = navController)
    }
}

class HomeState(
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
}