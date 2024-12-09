package com.yapp.chaeum.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yapp.chaeum.navigation.AppNavHost
import com.yapp.chaeum.navigation.TopLevelDestination
import kotlin.reflect.KClass

@Composable
fun App(appState: AppState = rememberAppState()) {
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 현재 화면이 TopLevelDestination 중 하나인지 확인
    val isTopLevel = TopLevelDestination.entries.any {
        currentDestination?.route == it.route.qualifiedName
    }

    Scaffold(
        bottomBar = {
            if (isTopLevel) {
                BottomNavigation(
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    TopLevelDestination.entries.forEach { topLevelRoute ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    topLevelRoute.selectedIcon,
                                    contentDescription = topLevelRoute.name
                                )
                            },
                            label = { Text(topLevelRoute.name) },
                            selected = currentDestination?.hierarchy?.any { destination ->
                                destination.route == topLevelRoute.route.qualifiedName
                            } == true,
                            onClick = {
                                appState.navigateToTopLevelDestination(topLevelRoute)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            appState = appState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private fun NavDestination.hasRoute(routeClass: KClass<*>): Boolean {
    return this.route == routeClass.qualifiedName
}