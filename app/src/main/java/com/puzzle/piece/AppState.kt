package com.puzzle.piece

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberAppState(): AppState {
    return remember { AppState() }
}

class AppState() {
}