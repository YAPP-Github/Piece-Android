package com.puzzle.piece.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.EtcRoute
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MyPageRoute
import com.puzzle.navigation.Route
import com.puzzle.piece.navigation.AppNavHost
import com.puzzle.piece.navigation.TopLevelDestination
import kotlin.reflect.KClass

@Composable
fun App(
    appState: AppState = rememberAppState(),
    navController: NavHostController,
    modifier: Modifier = Modifier,
    navigateToTopLevelDestination: (Route) -> Unit,
) {
    val currentDestination = navController.currentBackStackEntryAsState()
        .value?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.isHideBottomNavigationRoute() == false) {
                AppBottomBar(
                    currentDestination = currentDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
        }
    ) { innerPadding ->
        val contentModifier = modifier.padding(innerPadding)

        AppNavHost(
            navController = navController,
            modifier = contentModifier,
        )
    }
}

@Composable
private fun AppBottomBar(
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (Route) -> Unit,
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding()
    ) {
        TopLevelDestination.topLevelDestinations.forEach { topLevelRoute ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = topLevelRoute.selectedIcon,
                        contentDescription = topLevelRoute.name
                    )
                },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination.isRouteInHierarchy(topLevelRoute.route),
                onClick = {
                    when (topLevelRoute) {
                        TopLevelDestination.MATCHING -> navigateToTopLevelDestination(MatchingGraph)
                        TopLevelDestination.MY_PAGE -> navigateToTopLevelDestination(MyPageRoute)
                        TopLevelDestination.ETC -> navigateToTopLevelDestination(EtcRoute)
                    }
                },
            )
        }
    }
}

/**
 * 현재 목적지가 바텀 네비게이션이 보여지지 않는 화면인지 확인하는 메서드
 */
private fun NavDestination?.isHideBottomNavigationRoute(): Boolean =
    this?.hierarchy?.any { destination ->
        destination.route == AuthGraph::class.qualifiedName
    } ?: false

/**
 * 현재 목적지가 TopLevelDestination 라우트에 속하는지 확인하는 메서드
 */
private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false