package com.yapp.chaeum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.auth.navigation.AuthRoute
import com.example.auth.navigation.authScreen
import com.example.matching.navigation.matchingScreen
import com.example.mypage.navigation.myPageScreen
import com.yapp.chaeum.ui.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = AuthRoute,
        modifier = modifier,
    ) {
        authScreen(
            onLoginSuccess = { appState.loginSuccess() },
        )
        matchingScreen()
        myPageScreen()
    }
}