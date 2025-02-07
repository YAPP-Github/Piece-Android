package com.puzzle.matching.graph.report.contract

import com.airbnb.mvrx.MavericksState

data class ReportState(
    val isLoading: Boolean = false,
) : MavericksState
