package com.puzzle.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val PrimaryDefault = Color(0xFF6F00FB)
private val PrimaryMiddle = Color(0xFFD0ABFD)
private val PrimaryLight = Color(0xFFF6EFFF)

private val SubDefault = Color(0xFFFF7490)
private val SubMiddle = Color(0xFFFFB9C7)
private val SubLight = Color(0xFFFFE3E9)

private val Black = Color(0xFF1B1A2A)
private val Dark1 = Color(0xFF484B4D)
private val Dark2 = Color(0xFF6C7073)
private val Dark3 = Color(0xFF909599)
private val Light1 = Color(0xFFCBD1D9)
private val Light2 = Color(0xFFE8EBF0)
private val Light3 = Color(0xFFF4F6FA)
private val White = Color(0xFFFFFFFF)

private val Error = Color(0xFFFF3059)

@Immutable
data class PieceColors(
    val primaryDefault: Color = PrimaryDefault,
    val primaryMiddle: Color = PrimaryMiddle,
    val primaryLight: Color = PrimaryLight,
    val subDefault: Color = SubDefault,
    val subMiddle: Color = SubMiddle,
    val subLight: Color = SubLight,
    val black: Color = Black,
    val dark1: Color = Dark1,
    val dark2: Color = Dark2,
    val dark3: Color = Dark3,
    val light1: Color = Light1,
    val light2: Color = Light2,
    val light3: Color = Light3,
    val white: Color = White,
    val error: Color = Error,
)
