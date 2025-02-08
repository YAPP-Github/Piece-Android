package com.puzzle.matching.graph.block.contract

sealed class BlockIntent {
    data object OnBackClick : BlockIntent()
    data class OnBlockButtonClick(val userId: Int) : BlockIntent()
    data object OnBlockDoneClick : BlockIntent()
}
