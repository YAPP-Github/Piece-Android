package com.puzzle.matching.graph.block.contract

sealed class BlockIntent {
    data object NavigateToReportDetail : BlockIntent()
}
