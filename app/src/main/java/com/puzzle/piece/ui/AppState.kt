package com.puzzle.piece.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberAppState(): AppState {
    return remember { AppState() }
}

class AppState() {
}