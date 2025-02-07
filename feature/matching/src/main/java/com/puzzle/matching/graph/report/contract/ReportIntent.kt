package com.puzzle.matching.graph.report.contract

sealed class ReportIntent {
    data object NavigateToReportDetail : ReportIntent()
}
