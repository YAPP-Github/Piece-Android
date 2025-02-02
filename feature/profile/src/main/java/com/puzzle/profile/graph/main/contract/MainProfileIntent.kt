package com.puzzle.profile.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class MainProfileIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : MainProfileIntent()
    data object OnBasicProfileClick : MainProfileIntent()
    data object OnValueTalkClick : MainProfileIntent()
    data object OnValuePickClick : MainProfileIntent()
}