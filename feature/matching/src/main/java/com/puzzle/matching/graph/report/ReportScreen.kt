package com.puzzle.matching.graph.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.matching.graph.report.contract.ReportState

@Composable
internal fun ReportRoute(
    viewModel: ReportViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    ReportScreen(
        state = state,
    )
}

@Composable
internal fun ReportScreen(
    state: ReportState,
) {
}
