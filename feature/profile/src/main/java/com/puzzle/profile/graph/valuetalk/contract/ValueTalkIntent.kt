package com.puzzle.profile.graph.valuetalk.contract

import com.puzzle.navigation.NavigationEvent

sealed class ValueTalkIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : ValueTalkIntent()
}