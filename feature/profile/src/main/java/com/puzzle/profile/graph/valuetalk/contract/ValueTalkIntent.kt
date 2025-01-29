package com.puzzle.profile.graph.valuetalk.contract

sealed class ValueTalkIntent {
    data object OnBackClick : ValueTalkIntent()
}