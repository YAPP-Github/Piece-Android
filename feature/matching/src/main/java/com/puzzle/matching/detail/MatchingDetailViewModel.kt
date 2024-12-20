package com.puzzle.matching.detail

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow

data class MatchingDetailState(
    val isLoading: Boolean = false,
) : MavericksState

class MatchingDetailViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingDetailState,
) : MavericksViewModel<MatchingDetailState>(initialState) {

    private val _sideEffect = Channel<MatchingDetailSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    internal fun processIntent(intent: MatchingDetailIntent) {
        when (intent) {
            else -> Unit
        }
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

sealed class MatchingDetailIntent

sealed class MatchingDetailSideEffect