@file:OptIn(ExperimentalMaterial3Api::class)

package com.puzzle.piece.ui

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.puzzle.navigation.EtcRoute
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MatchingGraphDest.MatchingDetailRoute
import com.puzzle.navigation.MyPageRoute
import com.puzzle.navigation.Route
import com.puzzle.piece.navigation.AppNavHost
import com.puzzle.piece.navigation.TopLevelDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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
        containerColor = PieceTheme.colors.white,
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
        elevation = 0.dp,
        backgroundColor = PieceTheme.colors.white,
        modifier = Modifier.navigationBarsPadding(),
    ) {
        TopLevelDestination.topLevelDestinations.forEach { topLevelRoute ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(topLevelRoute.iconDrawableId),
                        contentDescription = topLevelRoute.contentDescription,
                    )
                },
                label = {
                    Text(
                        text = topLevelRoute.title,
                        style = PieceTheme.typography.captionM,
                    )
                },
                selected = currentDestination.isRouteInHierarchy(topLevelRoute.route),
                selectedContentColor = PieceTheme.colors.primaryDefault,
                unselectedContentColor = PieceTheme.colors.dark3,
                interactionSource = remember { NoRippleInteractionSource() },
                onClick = {
                    when (topLevelRoute) {
                        TopLevelDestination.MATCHING -> navigateToTopLevelDestination(
                            MatchingGraph
                        )

                        TopLevelDestination.MY_PAGE -> navigateToTopLevelDestination(MyPageRoute)
                        TopLevelDestination.ETC -> navigateToTopLevelDestination(EtcRoute)
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
