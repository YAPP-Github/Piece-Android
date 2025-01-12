package com.puzzle.auth.graph.login.contract

import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.navigation.NavigationEvent

sealed class LoginIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : LoginIntent()
    data class LoginOAuth(val oAuthProvider: OAuthProvider) : LoginIntent()
}
