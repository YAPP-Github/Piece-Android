package com.puzzle.matching.graph.block.contract

import com.airbnb.mvrx.MavericksState

data class BlockState(
    val userName: String = "",
) : MavericksState
