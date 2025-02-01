package com.puzzle.profile.graph.valuepick.contract

import com.airbnb.mvrx.MavericksState

data class ValuePickState(
    val a: String = ""
) : MavericksState