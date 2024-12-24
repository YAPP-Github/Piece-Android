package com.puzzle.matching.detail.contract

import com.airbnb.mvrx.MavericksState

data class MatchingDetailState(
    val isLoading: Boolean = false,
) : MavericksState