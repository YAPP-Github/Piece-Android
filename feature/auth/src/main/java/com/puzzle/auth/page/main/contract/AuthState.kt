package com.puzzle.auth.page.main.contract

import com.airbnb.mvrx.MavericksState

data class AuthState(
    val a: Boolean,
) : MavericksState