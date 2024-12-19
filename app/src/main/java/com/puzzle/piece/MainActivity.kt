package com.puzzle.piece

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.puzzle.designsystem.foundation.LocalColors
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.NavigationEvent
import com.puzzle.piece.ui.App
import com.puzzle.piece.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            viewModel.apply {
                val appState = rememberAppState()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        navigationHelper.navigationFlow.collect { event ->
                            handleNavigationEvent(
                                navController = navController,
                                event = event,
                            )
                        }
                    }
                }

                PieceTheme {
                    App(
                        appState = appState,
                        navController = navController,
                        navigateToTopLevelDestination = { topLevelDestination ->
                            navigationHelper.navigate(
                                NavigationEvent.TopLevelNavigateTo(topLevelDestination)
                            )
                        }
                    )
                }
            }
        }
    }

    private fun handleNavigationEvent(
        navController: NavController,
        event: NavigationEvent
    ) {
        when (event) {
            is NavigationEvent.NavigateTo -> {
                val navOptions = event.popUpTo?.let {
                    navOptions {
                        popUpTo(it) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                navController.navigate(
                    route = event.route,
                    navOptions = navOptions
                )
            }

            is NavigationEvent.NavigateUp -> navController.navigateUp()

            is NavigationEvent.TopLevelNavigateTo -> {
                val topLevelNavOptions = navOptions {
                    popUpTo(MatchingGraph) {
                        saveState = true
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                navController.navigate(
                    route = event.route,
                    navOptions = topLevelNavOptions
                )
            }
        }
    }
}