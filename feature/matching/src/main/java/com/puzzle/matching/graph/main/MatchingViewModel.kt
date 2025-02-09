package com.puzzle.matching.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.matching.graph.main.contract.MatchingIntent
import com.puzzle.matching.graph.main.contract.MatchingSideEffect
import com.puzzle.matching.graph.main.contract.MatchingState
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent.NavigateTo
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

class MatchingViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingState,
    private val navigationHelper: NavigationHelper,
    internal val eventHelper: EventHelper,
) : MavericksViewModel<MatchingState>(initialState) {
    private val intents = Channel<MatchingIntent>(BUFFERED)
    private val _sideEffect = Channel<MatchingSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: MatchingIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: MatchingIntent) {
        when (intent) {
            MatchingIntent.NavigateToReportDetail -> navigateToMatchingDetail()
        }
    }

    private fun handleSideEffect(sideEffect: MatchingSideEffect) {
        when (sideEffect) {
            else -> Unit
        }
    }

    private fun navigateToMatchingDetail() =
        navigationHelper.navigate(NavigateTo(MatchingGraphDest.MatchingDetailRoute))

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingViewModel, MatchingState> {
        override fun create(state: MatchingState): MatchingViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingViewModel, MatchingState> by hiltMavericksViewModelFactory()
}
