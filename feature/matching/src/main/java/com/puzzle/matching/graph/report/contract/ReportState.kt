package com.puzzle.matching.graph.report.contract

import com.airbnb.mvrx.MavericksState

data class ReportState(
    val isReportDone: Boolean = false,
    val selectedReason: ReportReason? = null
) : MavericksState {
    enum class ReportReason(val label: String) {
        INAPPROPRIATE_INTRODUCTION("소개글에서 불쾌감을 느꼈어요."),
        FALSE_PROFILE("프로필에 거짓이 포함되어 있어요."),
        INAPPROPRIATE_MEETING_PURPOSE("부적절한 만남을 추구하고 있어요."),
        OTHER("기타"),
    }
}
