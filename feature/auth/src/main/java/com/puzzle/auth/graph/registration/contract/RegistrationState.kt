package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState

data class RegistrationState(
    val a: Boolean = false,
) : MavericksState