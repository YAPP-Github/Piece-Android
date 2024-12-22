package com.puzzle.matching

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.matching.contract.MatchingIntent
import com.puzzle.matching.contract.MatchingSideEffect
import com.puzzle.matching.contract.MatchingState
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
    val navigationHelper: NavigationHelper,
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
            else -> Unit
        }
    }

    private fun handleSideEffect(sideEffect: MatchingSideEffect) {
        when (sideEffect) {
            else -> Unit
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingViewModel, MatchingState> {
        override fun create(state: MatchingState): MatchingViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingViewModel, MatchingState> by hiltMavericksViewModelFactory()
}



