package com.puzzle.matching.contract

sealed class MatchingIntent {
    data object NavigateToMatchingDetail : MatchingIntent()
}