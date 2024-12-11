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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yapp.chaeum.navigation.HomeNavHost
import com.yapp.chaeum.navigation.TopLevelDestination
import kotlinx.serialization.Serializable

@Serializable
data object HomeGraph

@Composable
fun HomeRoute(
    homeState: HomeState = rememberHomeState()
) {
    Scaffold(
        bottomBar = {
            HomeBottomBar(homeState)
        }
    ) { innerPadding ->
        HomeNavHost(
            navController = homeState.navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeBottomBar(
    homeState: HomeState,
) {
    val navBackStackEntry by homeState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding()
    ) {
        TopLevelDestination.entries.forEach { topLevelRoute ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = topLevelRoute.selectedIcon,
                        contentDescription = topLevelRoute.name
                    )
                },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any { destination ->
                    destination.route == topLevelRoute.route.qualifiedName
                } == true,
                onClick = {
                    homeState.navigateToTopLevelDestination(topLevelRoute)
                }
            )
        }
    }
}
