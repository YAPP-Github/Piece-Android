package com.puzzle.matching.graph.report

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.matching.graph.report.contract.ReportIntent
import com.puzzle.matching.graph.report.contract.ReportSideEffect
import com.puzzle.matching.graph.report.contract.ReportState
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ReportViewModel @AssistedInject constructor(
    @Assisted initialState: ReportState,
    internal val navigationHelper: NavigationHelper,
) : MavericksViewModel<ReportState>(initialState) {
    private val intents = Channel<ReportIntent>(BUFFERED)
    private val _sideEffects = Channel<ReportSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: ReportIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: ReportIntent) {
        when (intent) {
            ReportIntent.OnBackClick -> _sideEffects.send(
                ReportSideEffect.Navigate(NavigationEvent.NavigateUp)
            )

            ReportIntent.OnReportButtonClick -> reportUser()
            ReportIntent.OnReportDoneClick -> _sideEffects.send(
                ReportSideEffect.Navigate(
                    NavigationEvent.NavigateTo(
                        route = MatchingGraphDest.MatchingRoute,
                        popUpTo = true,
                    )
                )
            )

            is ReportIntent.SelectReportReason -> setState {
                copy(selectedReason = if (selectedReason != intent.reason) intent.reason else null)
            }
        }
    }

    private fun reportUser() {
        // Todo
        setState {
            copy(isReportDone = true)
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ReportViewModel, ReportState> {
        override fun create(state: ReportState): ReportViewModel
    }

    companion object :
        MavericksViewModelFactory<ReportViewModel, ReportState> by hiltMavericksViewModelFactory()
}
