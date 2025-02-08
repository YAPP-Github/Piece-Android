package com.puzzle.matching.graph.block

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.matching.graph.block.contract.BlockIntent
import com.puzzle.matching.graph.block.contract.BlockSideEffect
import com.puzzle.matching.graph.block.contract.BlockState
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

class BlockViewModel @AssistedInject constructor(
    @Assisted initialState: BlockState,
    internal val navigationHelper: NavigationHelper,
) : MavericksViewModel<BlockState>(initialState) {
    private val intents = Channel<BlockIntent>(BUFFERED)
    private val _sideEffect = Channel<BlockSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: BlockIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: BlockIntent) {
        when (intent) {
            BlockIntent.OnBackClick -> _sideEffect.send(BlockSideEffect.Navigate(NavigationEvent.NavigateUp))
            BlockIntent.OnBlockButtonClick -> blockUser()
            BlockIntent.OnBlockDoneClick -> _sideEffect.send(
                BlockSideEffect.Navigate(
                    NavigationEvent.NavigateTo(
                        route = MatchingGraphDest.MatchingRoute,
                        popUpTo = MatchingGraphDest.BlockRoute(),
                    )
                )
            )
        }
    }

    private fun blockUser() {
        // Todo

        setState {
            copy(isBlockDone = true)
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<BlockViewModel, BlockState> {
        override fun create(state: BlockState): BlockViewModel
    }

    companion object :
        MavericksViewModelFactory<BlockViewModel, BlockState> by hiltMavericksViewModelFactory()
}

