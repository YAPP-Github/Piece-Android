package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState

data class AuthVerficationState(
    val a: Boolean = false,
) : MavericksState