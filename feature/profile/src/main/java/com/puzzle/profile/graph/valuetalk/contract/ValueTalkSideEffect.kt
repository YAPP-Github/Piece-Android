package com.puzzle.profile.graph.valuetalk.contract

import com.puzzle.navigation.NavigationEvent

sealed class ValueTalkSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : ValueTalkSideEffect()
}
