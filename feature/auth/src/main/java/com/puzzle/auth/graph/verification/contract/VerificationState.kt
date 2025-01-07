package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState

data class VerificationState(
    val a: Boolean = false,
) : MavericksState