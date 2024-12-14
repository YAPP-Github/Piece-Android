package com.yapp.chaeum.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.etc.navigation.navigateToEtc
import com.example.matching.navigation.MatchingGraph
import com.example.matching.navigation.navigateToMatching
import com.example.mypage.navigation.navigateToMyPage
import com.yapp.chaeum.navigation.HomeGraph
import com.yapp.chaeum.navigation.TopLevelDestination

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

    fun navigateToTopLevelDestination(
        topLevelDestination: TopLevelDestination,
    ) {
        Log.d(
            "whatisthis",
            "topLevelNavOptions ${navController.graph.findStartDestination().id}, $topLevelDestination"
        )
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(HomeGraph) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.MATCHING -> navController.navigateToMatching(topLevelNavOptions)
            TopLevelDestination.MYPAGE -> navController.navigateToMyPage(topLevelNavOptions)
            TopLevelDestination.ETC -> navController.navigateToEtc(topLevelNavOptions)
        }
    }
}