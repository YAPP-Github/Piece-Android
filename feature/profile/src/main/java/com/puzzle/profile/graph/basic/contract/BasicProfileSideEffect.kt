package com.puzzle.profile.graph.basic.contract

import com.puzzle.navigation.NavigationEvent

sealed class BasicProfileSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : BasicProfileSideEffect()
}
