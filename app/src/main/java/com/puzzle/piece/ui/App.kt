package com.puzzle.piece.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.puzzle.auth.navigation.AuthGraph
import com.puzzle.piece.navigation.AppNavHost
import com.puzzle.piece.navigation.TopLevelDestination
import kotlin.reflect.KClass

@Composable
fun App(
    appState: AppState = rememberAppState(),
    modifier: Modifier = Modifier,
) {
    val currentDestination = appState.currentDestination

    Scaffold(
        bottomBar = {
            if (currentDestination?.isInAuthGraph() == false) {
                AppBottomBar(appState, currentDestination)
            }
        }
    ) { innerPadding ->
        val contentModifier = modifier.padding(innerPadding)
        AppNavHost(
            appState = appState,
            modifier = contentModifier,
        )
    }
}

@Composable
private fun AppBottomBar(
    appState: AppState,
    currentDestination: NavDestination?
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
                    appState.navigateToTopLevelDestination(topLevelRoute)
                }
            )
        }
    }
}

/**
 * 현재 목적지가 AuthGraph 인지 확인하는 메서드
 */
private fun NavDestination?.isInAuthGraph(): Boolean =
    this?.hierarchy?.any { destination ->
        destination.route == AuthGraph::class.qualifiedName
    } ?: false

/**
 * 현재 목적지가 TopLeveLDestination 라우트에 속하는지 확인하는 메서드
 */
private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false