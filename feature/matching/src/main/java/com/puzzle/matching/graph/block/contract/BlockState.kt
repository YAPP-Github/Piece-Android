package com.puzzle.matching.graph.block.contract

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState

data class BlockState(
    @PersistState val userId: Int = -1,
    @PersistState val userName: String = "",
    val isBlockDone: Boolean = false,
) : MavericksState
