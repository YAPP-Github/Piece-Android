package com.puzzle.profile.graph.valuepick.contract

import com.puzzle.navigation.NavigationEvent

sealed class ValuePickSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : ValuePickSideEffect()
}
