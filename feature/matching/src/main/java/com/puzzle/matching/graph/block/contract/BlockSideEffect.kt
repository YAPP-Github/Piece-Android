package com.puzzle.matching.graph.block.contract

import com.puzzle.navigation.NavigationEvent

sealed class BlockSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : BlockSideEffect()
}
