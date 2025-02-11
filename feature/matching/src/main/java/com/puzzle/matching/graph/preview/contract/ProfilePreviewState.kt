package com.puzzle.matching.graph.preview.contract

import com.airbnb.mvrx.MavericksState

data class ProfilePreviewState(
    val isLoading: Boolean = false,
) : MavericksState