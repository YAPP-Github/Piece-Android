package com.puzzle.matching.graph.report

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.matching.graph.report.contract.ReportState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ReportViewModel @AssistedInject constructor(
    @Assisted initialState: ReportState,
    internal val navigationHelper: NavigationHelper,
) : MavericksViewModel<ReportState>(initialState) {

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ReportViewModel, ReportState> {
        override fun create(state: ReportState): ReportViewModel
    }

    companion object :
        MavericksViewModelFactory<ReportViewModel, ReportState> by hiltMavericksViewModelFactory()
}

