package com.puzzle.matching.graph.report.contract

sealed class ReportIntent {
    data object OnBackClick : ReportIntent()
    data class OnReportButtonClick(val userId: Int, val reason: String) : ReportIntent()
    data object OnReportDoneClick : ReportIntent()
    data class SelectReportReason(val reason: ReportState.ReportReason) : ReportIntent()
}
