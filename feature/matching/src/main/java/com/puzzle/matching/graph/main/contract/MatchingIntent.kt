package com.puzzle.matching.graph.main.contract

sealed class MatchingIntent {
    data object NavigateToMatchingDetail : MatchingIntent()
}