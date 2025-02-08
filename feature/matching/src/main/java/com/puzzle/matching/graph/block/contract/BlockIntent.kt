package com.puzzle.matching.graph.block.contract

sealed class BlockIntent {
    data object OnBackClick : BlockIntent()
    data object OnBlockButtonClick : BlockIntent()
    data object OnBlockDoneClick : BlockIntent()
}
