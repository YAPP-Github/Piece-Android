package com.puzzle.profile.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class MainProfileSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : MainProfileSideEffect()
}
