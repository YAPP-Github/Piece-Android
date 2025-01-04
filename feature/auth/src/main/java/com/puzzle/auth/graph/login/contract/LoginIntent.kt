package com.puzzle.auth.graph.login.contract

import com.puzzle.navigation.NavigationEvent

sealed class LoginIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : LoginIntent()
}