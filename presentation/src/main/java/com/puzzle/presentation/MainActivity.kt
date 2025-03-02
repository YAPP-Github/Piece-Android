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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.puzzle.analytics.AnalyticsHelper
import com.puzzle.analytics.LocalAnalyticsHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.component.PieceModalBottomSheet
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.user.UserRole
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.presentation.network.NetworkMonitor
import com.puzzle.presentation.network.NetworkScreen
import com.puzzle.presentation.ui.App
import com.puzzle.presentation.update.ForceUpdateDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { !viewModel.isInitialized.value }
        enableEdgeToEdge()
        blockScreenShot()

        setContent {
            viewModel.apply {
                val networkState by networkMonitor.networkState.collectAsStateWithLifecycle()
                val forceUpdate by viewModel.forceUpdate.collectAsStateWithLifecycle()
                val userRole by viewModel.userRole.collectAsStateWithLifecycle()
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
                                    is PieceEvent.ShowSnackBar -> scope.launch {
                                        snackBarHostState.currentSnackbarData?.dismiss()
                                        snackBarHostState.showSnackbar("${event.msg}/${event.type}")
                                    }

                                    PieceEvent.HideSnackBar -> snackBarHostState.currentSnackbarData?.dismiss()

                                    is PieceEvent.ShowBottomSheet -> scope.launch {
                                        bottomSheetContent = event.content
                                        sheetState.show()
                                    }

                                    PieceEvent.HideBottomSheet -> scope.launch { sheetState.hide() }
                                }
                            }
                        }
                    }
                }

                PieceTheme {
                    CompositionLocalProvider(LocalAnalyticsHelper provides analyticsHelper) {
                        PieceModalBottomSheet(
                            sheetState = sheetState,
                            sheetContent = bottomSheetContent,
                        ) {
                            App(
                                snackBarHostState = snackBarHostState,
                                navController = navController,
                                navigateToBottomNaviNaviateTo = { bottomNaviDestination ->
                                    if (bottomNaviDestination == ProfileGraphDest.MainProfileRoute &&
                                        userRole != UserRole.USER
                                    ) {
                                        eventHelper.sendEvent(PieceEvent.ShowSnackBar(msg = "아직 심사 중입니다."))
                                        return@App
                                    }

                                    navigationHelper.navigate(
                                        NavigationEvent.BottomNaviTo(bottomNaviDestination)
                                    )
                                }
                            )

                            NetworkScreen(networkState)
                            ForceUpdateDialog(forceUpdate)
                        }
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
            is NavigationEvent.To -> {
                val navOptions = navOptions {
                    if (event.popUpTo) {
                        popUpTo(
                            navController.currentBackStackEntry?.destination?.route
                                ?: navController.graph.startDestinationRoute
                                ?: AuthGraph.toString()
                        ) { inclusive = true }
                    }
                    launchSingleTop = true
                }

                navController.navigate(
                    route = event.route,
                    navOptions = navOptions
                )
            }

            is NavigationEvent.Up -> navController.navigateUp()

            is NavigationEvent.TopLevelTo -> {
                val topLevelNavOptions = navOptions {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }

                navController.navigate(
                    route = event.route,
                    navOptions = topLevelNavOptions
                )
            }

            is NavigationEvent.BottomNaviTo -> {
                val topLevelNavOptions = navOptions {
                    popUpTo(MatchingGraphDest.MatchingRoute) {
                        saveState = true
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

    private fun blockScreenShot() {
        if (!BuildConfig.DEBUG) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
