package com.puzzle.auth.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class AuthIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : AuthIntent()
}