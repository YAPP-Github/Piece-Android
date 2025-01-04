package com.puzzle.matching.page.main.contract

sealed class MatchingIntent {
    data object NavigateToMatchingDetail : MatchingIntent()
}