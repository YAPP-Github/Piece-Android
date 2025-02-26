package com.puzzle.designsystem.foundation

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LocalColors = staticCompositionLocalOf {
    PieceColors()
}
private val LocalTypography = staticCompositionLocalOf {
    PieceTypography()
}

@Composable
fun PieceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(content = content)
}

@Composable
fun StatusBarColor(color: Color) {
    val view = LocalView.current
    val useDarkIcons = color.luminance() > 0.5f
    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            window.statusBarColor = color.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                useDarkIcons
        }
    }
}

@Composable
fun NavigationBarColor(color: Color) {
    val view = LocalView.current
    val useDarkIcons = color.luminance() > 0.5f
    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            window.navigationBarColor = color.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                useDarkIcons
        }
    }
}


object PieceTheme {
    val colors: PieceColors
        @Composable
        get() = LocalColors.current
    val typography: PieceTypography
        @Composable
        get() = LocalTypography.current
}
