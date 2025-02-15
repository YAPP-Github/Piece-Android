package com.puzzle.matching.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class MatchingSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : MatchingSideEffect()
}