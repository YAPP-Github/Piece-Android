package com.puzzle.matching.graph.main.contract

sealed class MatchingIntent {
    data object NavigateToReportDetail : MatchingIntent()
}
