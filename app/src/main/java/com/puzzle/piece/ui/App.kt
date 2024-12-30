@file:OptIn(ExperimentalMaterial3Api::class)

package com.puzzle.piece.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.bottomAppBarFabElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.puzzle.common.ui.NoRippleInteractionSource
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MatchingGraphDest.MatchingDetailRoute
import com.puzzle.navigation.MyPageRoute
import com.puzzle.navigation.Route
import com.puzzle.navigation.SettingRoute
import com.puzzle.piece.R
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
            if (currentDestination?.shouldHideBottomNavigation() == false) {
                AppBottomBar(
                    currentDestination = currentDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
        },
        floatingActionButton = {
            if (currentDestination?.shouldHideBottomNavigation() == false) {
                FloatingActionButton(
                    onClick = { navigateToTopLevelDestination(MatchingGraph) },
                    containerColor = PieceTheme.colors.white,
                    shape = CircleShape,
                    elevation = bottomAppBarFabElevation(),
                    modifier = Modifier.offset(y = 84.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_matching),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .background(PieceTheme.colors.white),
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
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
    NavigationBar(
        containerColor = PieceTheme.colors.white,
        modifier = Modifier
            .navigationBarsPadding()
            .height(68.dp),
    ) {
        TopLevelDestination.topLevelDestinations.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 2.dp),
                    ) {
                        Icon(
                            painter = painterResource(topLevelRoute.iconDrawableId),
                            contentDescription = topLevelRoute.contentDescription,
                            modifier = Modifier.size(32.dp),
                        )

                        Text(
                            text = topLevelRoute.title,
                            style = PieceTheme.typography.captionM,
                        )
                    }
                },
                alwaysShowLabel = false,
                selected = currentDestination.isRouteInHierarchy(topLevelRoute.route),
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = PieceTheme.colors.primaryDefault,
                    unselectedIconColor = PieceTheme.colors.dark3,
                    selectedTextColor = PieceTheme.colors.primaryDefault,
                    unselectedTextColor = PieceTheme.colors.dark3,
                    indicatorColor = Color.Transparent,
                ),
                interactionSource = remember { NoRippleInteractionSource() },
                onClick = {
                    when (topLevelRoute) {
                        TopLevelDestination.MATCHING -> navigateToTopLevelDestination(
                            MatchingGraph
                        )

                        TopLevelDestination.MY_PAGE -> navigateToTopLevelDestination(MyPageRoute)
                        TopLevelDestination.SETTING -> navigateToTopLevelDestination(
                            SettingRoute
                        )
                    }
                },
            )
        }
    }
}

private val HIDDEN_BOTTOM_NAV_ROUTES = setOf(
    AuthGraph::class.qualifiedName,
    MatchingDetailRoute::class.qualifiedName,
)

private fun NavDestination?.shouldHideBottomNavigation(): Boolean =
    this?.hierarchy?.any { destination -> destination.route in HIDDEN_BOTTOM_NAV_ROUTES } ?: false

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true
