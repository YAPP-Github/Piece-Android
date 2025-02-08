package com.puzzle.profile.graph.valuepick.contract

sealed class ValuePickIntent {
    data object OnBackClick : ValuePickIntent()
}