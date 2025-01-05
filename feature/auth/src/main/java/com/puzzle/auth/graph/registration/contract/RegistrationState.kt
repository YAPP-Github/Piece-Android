package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.terms.Term
import java.time.LocalDateTime

data class RegistrationState(
    val terms: List<Term> = listOf(
        Term(
            termId = 1,
            title = "서비스 이용약관",
            content = "서비스 이용에 대한 약관 내용입니다.",
            required = true,
            startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        ),
        Term(
            termId = 2,
            title = "개인정보 처리방침",
            content = "개인정보 보호에 대한 약관 내용입니다.",
            required = true,
            startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        ),
        Term(
            termId = 3,
            title = "위치 정보 이용약관",
            content = "위치 정보 활용에 대한 약관 내용입니다.",
            required = false,
            startDate = LocalDateTime.parse("2024-01-01T00:00:00")
        ),
    ),
    val termsCheckedInfo: MutableMap<Int, Boolean> = mutableMapOf(),
) : MavericksState {
    val agreeAllTerms = terms.all { termsCheckedInfo.getOrDefault(it.termId, false) }
}
