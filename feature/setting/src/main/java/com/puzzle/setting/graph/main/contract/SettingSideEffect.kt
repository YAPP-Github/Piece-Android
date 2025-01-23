package com.puzzle.setting.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class SettingSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : SettingSideEffect()
}
