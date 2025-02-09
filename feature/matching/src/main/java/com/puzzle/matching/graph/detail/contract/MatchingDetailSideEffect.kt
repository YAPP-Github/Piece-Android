package com.puzzle.matching.graph.detail.contract

import com.puzzle.navigation.NavigationEvent

sealed class MatchingDetailSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : MatchingDetailSideEffect()
}
