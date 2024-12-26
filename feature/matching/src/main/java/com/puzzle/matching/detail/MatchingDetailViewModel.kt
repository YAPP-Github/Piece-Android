package com.puzzle.matching.detail

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.matching.detail.contract.MatchingDetailIntent
import com.puzzle.matching.detail.contract.MatchingDetailSideEffect
import com.puzzle.matching.detail.contract.MatchingDetailState
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

class MatchingDetailViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingDetailState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<MatchingDetailState>(initialState) {

    private val intents = Channel<MatchingDetailIntent>(BUFFERED)

    private val _sideEffect = Channel<MatchingDetailSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: MatchingDetailIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: MatchingDetailIntent) {
        when (intent) {
            MatchingDetailIntent.OnMatchingDetailCloseClick -> processOnMatchingDetailCloseClickIntent()
            MatchingDetailIntent.OnBackPageClick -> processOnBackPageClickIntent()
            MatchingDetailIntent.OnNextPageClick -> processOnNextPageClickIntent()
            MatchingDetailIntent.OnMoreClick -> TODO()
        }
    }

    private fun processOnNextPageClickIntent() {
        setState {
            val updatedPage = when (currentPage) {
                is MatchingDetailState.BasicInfoState -> MatchingDetailState.ValueTalkState()
                is MatchingDetailState.ValueTalkState -> MatchingDetailState.ValuePickState()
                is MatchingDetailState.ValuePickState -> MatchingDetailState.ValuePickState()
            }
            copy(currentPage = updatedPage)
        }
    }

    private fun processOnBackPageClickIntent() {
        setState {
            val updatedPage = when (currentPage) {
                is MatchingDetailState.BasicInfoState -> MatchingDetailState.BasicInfoState()
                is MatchingDetailState.ValueTalkState -> MatchingDetailState.BasicInfoState()
                is MatchingDetailState.ValuePickState -> MatchingDetailState.ValueTalkState()
            }
            copy(currentPage = updatedPage)
        }
    }

    private fun processOnMatchingDetailCloseClickIntent() {
        navigationHelper.navigate(NavigationEvent.NavigateUp)
    }

    private fun handleSideEffect(sideEffect: MatchingDetailSideEffect) {
        when (sideEffect) {
            else -> Unit
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingDetailViewModel, MatchingDetailState> {
        override fun create(state: MatchingDetailState): MatchingDetailViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingDetailViewModel, MatchingDetailState> by hiltMavericksViewModelFactory()
}

