package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.terms.TermInfo
import java.time.LocalDateTime

data class RegistrationState(
    val termInfos: List<TermInfo> = listOf(
        TermInfo(
            termId = 1,
            title = "서비스 이용약관",
            content = "서비스 이용에 대한 약관 내용입니다.",
            required = true,
            startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        ),
        TermInfo(
            termId = 2,
            title = "개인정보 처리방침",
            content = "개인정보 보호에 대한 약관 내용입니다.",
            required = true,
            startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        ),
        TermInfo(
            termId = 3,
            title = "위치 정보 이용약관",
            content = "위치 정보 활용에 대한 약관 내용입니다.",
            required = false,
            startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        ),
    ),
    val termsCheckedInfo: MutableMap<Int, Boolean> = mutableMapOf(),
) : MavericksState {
    val agreeAllTerms = termInfos.all { termsCheckedInfo.getOrDefault(it.termId, false) }
}
