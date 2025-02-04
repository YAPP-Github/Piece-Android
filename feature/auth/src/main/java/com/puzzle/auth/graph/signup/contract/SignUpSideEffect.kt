package com.puzzle.auth.graph.signup.contract

import com.puzzle.navigation.NavigationEvent

sealed class SignUpSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : SignUpSideEffect()
    data class ShowSnackBar(val msg: String) : SignUpSideEffect()
    data object HideSnackBar : SignUpSideEffect()
}
