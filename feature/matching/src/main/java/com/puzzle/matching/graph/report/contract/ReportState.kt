package com.puzzle.matching.graph.report.contract

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState

data class ReportState(
    @PersistState val userId: Int = -1,
    @PersistState val userName: String = "",
) : MavericksState
