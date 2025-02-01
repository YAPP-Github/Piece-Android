package com.puzzle.profile.graph.valuepick.contract

import com.puzzle.navigation.NavigationEvent

sealed class ValuePickIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : ValuePickIntent()
}