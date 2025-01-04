package com.puzzle.matching.page.main.contract

import com.airbnb.mvrx.MavericksState

data class MatchingState(
    val isLoading: Boolean = false,
) : MavericksState