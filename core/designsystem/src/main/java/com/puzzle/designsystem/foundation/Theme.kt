package com.puzzle.designsystem.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

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

object PieceTheme {
    val colors: PieceColors
        @Composable
        get() = LocalColors.current
    val typography: PieceTypography
        @Composable
        get() = LocalTypography.current
}
