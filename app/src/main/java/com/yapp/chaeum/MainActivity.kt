package com.yapp.chaeum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yapp.chaeum.ui.App
import com.yapp.chaeum.ui.rememberAppState
import com.yapp.chaeum.ui.theme.ChaeumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            ChaeumTheme {
                App(appState)
            }
        }
    }
}