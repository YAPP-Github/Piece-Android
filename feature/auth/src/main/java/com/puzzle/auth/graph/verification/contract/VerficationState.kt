package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState

data class VerficationState(
    val a: Boolean = false,
) : MavericksState