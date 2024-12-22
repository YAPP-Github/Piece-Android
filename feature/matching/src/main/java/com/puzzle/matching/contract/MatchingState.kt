package com.puzzle.matching.contract

import com.airbnb.mvrx.MavericksState

data class MatchingState(
    val isLoading: Boolean = false,
) : MavericksState