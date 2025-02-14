package com.puzzle.matching.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class MatchingIntent {
    data object OnButtonClick : MatchingIntent()
    data class Navigate(val navigationEvent: NavigationEvent) : MatchingIntent()
    data object OnEditProfileClick : MatchingIntent()
}
