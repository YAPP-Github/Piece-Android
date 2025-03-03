@file:OptIn(ExperimentalMaterial3Api::class)

package com.puzzle.presentation.ui

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.puzzle.analytics.TrackScreenViewEvent
import com.puzzle.common.ui.ANIMATION_DURATION
import com.puzzle.common.ui.NoRippleInteractionSource
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSnackBar
import com.puzzle.designsystem.component.PieceSnackBarHost
import com.puzzle.designsystem.foundation.NavigationBarColor
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.designsystem.foundation.StatusBarColor
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.OnboardingRoute
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.navigation.ProfileGraphDest.MainProfileRoute
import com.puzzle.navigation.Route
import com.puzzle.navigation.SettingGraph
import com.puzzle.navigation.SettingGraphDest
import com.puzzle.navigation.getRouteClassName
import com.puzzle.presentation.navigation.AppNavHost
import com.puzzle.presentation.navigation.TopLevelDestination
import kotlin.reflect.KClass

@Composable
fun App(
    snackBarHostState: SnackbarHostState,
    navController: NavHostController,
    navigateToBottomNaviDestination: (Route) -> Unit,
) {
    val currentDestination = navController.currentBackStackEntryAsState()
        .value?.destination

    Scaffold(
        snackbarHost = {
            PieceSnackBarHost(
                hostState = snackBarHostState,
                snackbar = { snackBarData -> PieceSnackBar(snackBarData) },
            )
        },
        containerColor = PieceTheme.colors.white,
        bottomBar = {
            AnimatedVisibility(
                visible = currentDestination?.shouldHideBottomNavigation() == false,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                AppBottomBar(
                    currentDestination = currentDestination,
                    navigateToBottomNaviDestination = navigateToBottomNaviDestination,
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentDestination?.shouldHideBottomNavigation() == false,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                FloatingActionButton(
                    onClick = { navigateToBottomNaviDestination(MatchingGraph) },
                    containerColor = PieceTheme.colors.white,
                    shape = CircleShape,
                    elevation = bottomAppBarFabElevation(),
                    modifier = Modifier.offset(y = 84.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_matching),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        val contentModifier = when {
            currentDestination?.route?.startsWith(
                MatchingGraphDest.MatchingDetailRoute::class.qualifiedName ?: ""
            ) == true || currentDestination?.route?.startsWith(
                MatchingGraphDest.ContactRoute::class.qualifiedName ?: ""
            ) == true || currentDestination?.route?.startsWith(
                MatchingGraphDest.ProfilePreviewRoute::class.qualifiedName ?: ""
            ) == true -> Modifier.consumeWindowInsets(innerPadding)

            else -> Modifier.padding(innerPadding)
        }

        AppNavHost(
            navController = navController,
            modifier = contentModifier,
        )
    }

    SystemBarColor(currentDestination)

    TrackScreenViewEvent(
        key = currentDestination,
        screenName = getRouteClassName(currentDestination?.route)
    )
}

@Composable
private fun AppBottomBar(
    currentDestination: NavDestination?,
    navigateToBottomNaviDestination: (Route) -> Unit,
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
                    if (topLevelRoute != TopLevelDestination.MATCHING) {
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
                        TopLevelDestination.MATCHING -> navigateToBottomNaviDestination(MatchingGraph)
                        TopLevelDestination.PROFILE -> navigateToBottomNaviDestination(
                            MainProfileRoute
                        )

                        TopLevelDestination.SETTING -> navigateToBottomNaviDestination(SettingGraph)
                    }
                },
            )
        }
    }
}

private val HIDDEN_BOTTOM_NAV_ROUTES = setOf(
    OnboardingRoute::class,
    AuthGraph::class,
    MatchingGraphDest.BlockRoute::class,
    MatchingGraphDest.ReportRoute::class,
    MatchingGraphDest.ContactRoute::class,
    MatchingGraphDest.ProfilePreviewRoute::class,
    MatchingGraphDest.MatchingDetailRoute::class,
    ProfileGraphDest.RegisterProfileRoute::class,
    ProfileGraphDest.ValueTalkProfileRoute::class,
    ProfileGraphDest.ValuePickProfileRoute::class,
    ProfileGraphDest.BasicProfileRoute::class,
    SettingGraphDest.WithdrawRoute::class,
    SettingGraphDest.WebViewRoute::class,
)

private fun NavDestination?.shouldHideBottomNavigation(): Boolean =
    this?.hierarchy?.any { destination ->
        HIDDEN_BOTTOM_NAV_ROUTES.any {
            destination.route?.startsWith(it.qualifiedName ?: "") == true
        }
    } ?: false

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true

@Composable
private fun SystemBarColor(currentDestination: NavDestination?) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    if (!view.isInEditMode) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    } else {
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }

    SetStatusBarColor(currentDestination)
    SetNavigationBarColor(currentDestination)
}

@Composable
private fun SetStatusBarColor(currentDestination: NavDestination?) {
    val statusBarColor by animateColorAsState(
        targetValue = when {
            currentDestination?.route?.startsWith(
                MatchingGraphDest.MatchingRoute::class.qualifiedName ?: ""
            ) == true -> PieceTheme.colors.black

            currentDestination?.route?.startsWith(
                MatchingGraphDest.MatchingDetailRoute::class.qualifiedName ?: ""
            ) == true || currentDestination?.route?.startsWith(
                MatchingGraphDest.ContactRoute::class.qualifiedName ?: ""
            ) == true || currentDestination?.route?.startsWith(
                MatchingGraphDest.ProfilePreviewRoute::class.qualifiedName ?: ""
            ) == true -> Color.Transparent

            else -> PieceTheme.colors.white
        },
        animationSpec = tween(ANIMATION_DURATION),
        label = "StatusBarColorAnimation"
    )

    StatusBarColor(statusBarColor)
}

@Composable
private fun SetNavigationBarColor(currentDestination: NavDestination?) {
    val navigationBarColor: Color by animateColorAsState(
        targetValue = when {
            currentDestination?.route?.startsWith(
                MatchingGraphDest.MatchingDetailRoute::class.qualifiedName ?: ""
            ) == true || currentDestination?.route?.startsWith(
                MatchingGraphDest.ContactRoute::class.qualifiedName ?: ""
            ) == true || currentDestination?.route?.startsWith(
                MatchingGraphDest.ProfilePreviewRoute::class.qualifiedName ?: ""
            ) == true -> Color.Transparent

            else -> PieceTheme.colors.white
        },
        animationSpec = tween(ANIMATION_DURATION),
        label = "NavigationBarColorAnimation"
    )

    NavigationBarColor(navigationBarColor)
}
