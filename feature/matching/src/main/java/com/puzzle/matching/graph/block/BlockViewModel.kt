package com.puzzle.matching.graph.block

import androidx.lifecycle.SavedStateHandle
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.matching.graph.block.contract.BlockState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class BlockViewModel @AssistedInject constructor(
    @Assisted initialState: BlockState,
    savedStateHandle: SavedStateHandle,
    internal val navigationHelper: NavigationHelper,
) : MavericksViewModel<BlockState>(initialState) {
    val userId = requireNotNull(savedStateHandle.get<String>("userId")) { "userId is required." }

    init {
        val userName =
            requireNotNull(savedStateHandle.get<String>("userName")) { "userName is required." }

        setState {
            copy(userName = userName)
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<BlockViewModel, BlockState> {
        override fun create(state: BlockState): BlockViewModel
    }

    companion object :
        MavericksViewModelFactory<BlockViewModel, BlockState> by hiltMavericksViewModelFactory()
}

