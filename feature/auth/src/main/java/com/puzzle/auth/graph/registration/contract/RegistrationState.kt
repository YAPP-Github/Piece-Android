package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.terms.Term

data class RegistrationState(
    val terms: List<Term> = emptyList(),
    val termsCheckedInfo: MutableMap<Int, Boolean> = mutableMapOf(),
) : MavericksState {
    val allTermsAgreed = terms.all { termsCheckedInfo.getOrDefault(it.termId, false) }
}
