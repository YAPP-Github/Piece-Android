package com.puzzle.piece

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.puzzle.common.event.PieceEvent
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.NavigationEvent
import com.puzzle.piece.ui.App
import com.puzzle.piece.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var snackbarHostState: SnackbarHostState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO(재확인 필요)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            viewModel.apply {
                val appState = rememberAppState()
                val navController = rememberNavController()
                snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        navigationHelper.navigationFlow.collect { event ->
                            handleNavigationEvent(
                                navController = navController,
                                event = event,
                            )
                        }
                    }

                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        eventHelper.eventFlow.collect { event ->
                            handlePieceEvent(event)
                        }
                    }
                }

                PieceTheme {
                    App(
                        appState = appState,
                        snackbarHostState = snackbarHostState,
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

    private suspend fun handlePieceEvent(event: PieceEvent) {
        when (event) {
            is PieceEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.msg)
        }
    }
}
