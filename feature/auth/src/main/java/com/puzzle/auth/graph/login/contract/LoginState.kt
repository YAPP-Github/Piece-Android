package com.puzzle.auth.graph.login.contract

import com.airbnb.mvrx.MavericksState

data class LoginState(
    val a: Boolean = false,
) : MavericksState