package com.yapp.chaeum.ui

import androidx.compose.runtime.Composable
import com.yapp.chaeum.navigation.AppNavHost

@Composable
fun App(appState: AppState = rememberAppState()) {
    AppNavHost(
        appState = appState,
    )
}
