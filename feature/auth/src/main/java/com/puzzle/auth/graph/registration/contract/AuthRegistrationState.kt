package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState

data class AuthRegistrationState(
    val a: Boolean = false,
) : MavericksState