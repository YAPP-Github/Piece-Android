package com.puzzle.matching

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

data class MatchingState(
    val isLoading: Boolean,
) : MavericksState

class MatchingViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingState,
) : MavericksViewModel<MatchingState>(initialState) {

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingViewModel, MatchingState> {
        override fun create(state: MatchingState): MatchingViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingViewModel, MatchingState> by hiltMavericksViewModelFactory()
}