package com.puzzle.piece

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.puzzle.piece.ui.App
import com.puzzle.piece.ui.rememberAppState
import com.puzzle.piece.ui.theme.PieceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            PieceTheme {
                App(appState)
            }
        }
    }
}