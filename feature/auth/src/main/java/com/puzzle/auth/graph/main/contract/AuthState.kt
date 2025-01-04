package com.puzzle.auth.graph.main.contract

import com.airbnb.mvrx.MavericksState

data class AuthState(
    val a: Boolean = false,
) : MavericksState