package com.puzzle.auth.graph.login.contract

import com.airbnb.mvrx.MavericksState

data class LoginState(
    val isLoading: Boolean = false,
) : MavericksState
