package com.puzzle.auth.graph.login.contract

sealed class LoginSideEffect {
    data object LoginKakao : LoginSideEffect()
}