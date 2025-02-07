package com.puzzle.matching.graph.main.contract

sealed class ReportIntent {
    data object NavigateToReportDetail : ReportIntent()
}
