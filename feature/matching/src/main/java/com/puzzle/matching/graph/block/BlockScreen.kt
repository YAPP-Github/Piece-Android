package com.puzzle.matching.graph.block

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.matching.graph.block.contract.BlockState

@Composable
internal fun BlockRoute(
    viewModel: BlockViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    BlockScreen(
        state = state,
    )
}

@Composable
internal fun BlockScreen(
    state: BlockState,
) {
}
