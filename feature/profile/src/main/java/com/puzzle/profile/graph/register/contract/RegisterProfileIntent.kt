package com.puzzle.profile.graph.register.contract

import com.puzzle.navigation.NavigationEvent

sealed class RegisterProfileIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : RegisterProfileIntent()
}