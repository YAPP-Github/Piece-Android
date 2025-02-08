package com.puzzle.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.puzzle.common.event.PieceEvent
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.component.PieceModalBottomSheet
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.NavigationEvent
import com.puzzle.presentation.ui.App
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        splashScreen.setKeepOnScreenCondition { !viewModel.isInitialized.value }

        // TODO(재확인 필요)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            viewModel.apply {
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }

                var bottomSheetContent by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }
                val scope = rememberCoroutineScope()
                val sheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    skipHalfExpanded = true,
                )

                LaunchedEffect(Unit) {
                    repeatOnStarted {
                        launch {
                            navigationHelper.navigationFlow.collect { event ->
                                handleNavigationEvent(
                                    navController = navController,
                                    event = event,
                                )
                            }
                        }

                        launch {
                            eventHelper.eventFlow.collect { event ->
                                when (event) {
                                    is PieceEvent.ShowSnackBar -> {
                                        scope.launch {
                                            snackBarHostState.currentSnackbarData?.dismiss()
                                            snackBarHostState.showSnackbar("${event.msg}/${event.type}")
                                        }
                                    }

                                    PieceEvent.HideSnackBar -> snackBarHostState.currentSnackbarData?.dismiss()

                                    is PieceEvent.ShowBottomSheet -> {
                                        scope.launch {
                                            bottomSheetContent = event.content
                                            sheetState.show()
                                        }
                                    }

                                    PieceEvent.HideBottomSheet -> {
                                        scope.launch {
                                            sheetState.hide()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                PieceTheme {
                    PieceModalBottomSheet(
                        sheetState = sheetState,
                        sheetContent = bottomSheetContent,
                    ) {
                        App(
                            snackBarHostState = snackBarHostState,
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
