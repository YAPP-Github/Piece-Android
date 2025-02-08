package com.puzzle.matching.graph.report.contract

import com.puzzle.navigation.NavigationEvent

sealed class ReportSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : ReportSideEffect()
}
