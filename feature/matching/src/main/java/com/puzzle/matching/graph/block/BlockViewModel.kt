package com.puzzle.matching.graph.block

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.MatchingRepository
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
    private val matchingRepository: MatchingRepository,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<BlockState>(initialState) {
    private val intents = Channel<BlockIntent>(BUFFERED)
    private val _sideEffects = Channel<BlockSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

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
            BlockIntent.OnBackClick -> _sideEffects.send(BlockSideEffect.Navigate(NavigationEvent.Up))
            is BlockIntent.OnBlockButtonClick -> blockUser(intent.userId)
            BlockIntent.OnBlockDoneClick -> _sideEffects.send(
                BlockSideEffect.Navigate(
                    NavigationEvent.To(
                        route = MatchingGraphDest.MatchingRoute,
                        popUpTo = true,
                    )
                )
            )
        }
    }

    private fun blockUser(userId: Int) = viewModelScope.launch {
        matchingRepository.blockUser(userId = userId)
            .onSuccess { setState { copy(isBlockDone = true) } }
            .onFailure { errorHelper.sendError(it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<BlockViewModel, BlockState> {
        override fun create(state: BlockState): BlockViewModel
    }

    companion object :
        MavericksViewModelFactory<BlockViewModel, BlockState> by hiltMavericksViewModelFactory()
}

