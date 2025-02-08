package com.puzzle.matching.graph.report.contract

sealed class ReportIntent {
    data object OnBackClick : ReportIntent()
    data object OnReportButtonClick : ReportIntent()
    data object OnReportDoneClick : ReportIntent()
    data class SelectReportReason(val reason: ReportState.ReportReason) : ReportIntent()
}
