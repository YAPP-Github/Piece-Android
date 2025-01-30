package com.puzzle.auth.graph.signup.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.terms.Term

data class SignUpState(
    val terms: List<Term> = emptyList(),
    val termsCheckedInfo: Map<Int, Boolean> = emptyMap(),
    val signUpPage: SignUpPage = SignUpPage.TermPage,
) : MavericksState {
    val areAllTermsAgreed = terms.filter { it.required }
        .all { termsCheckedInfo.getOrDefault(it.id, false) }

    enum class SignUpPage {
        TermPage,
        TermDetailPage,
        AccessRightsPage,
        AvoidAcquaintancesPage,
        SignUpCompleted;

        companion object {
            fun getPreviousPage(currentPage: SignUpPage): SignUpPage? {
                return when (currentPage) {
                    TermDetailPage -> TermPage
                    AccessRightsPage -> TermPage
                    AvoidAcquaintancesPage -> AccessRightsPage
                    else -> null
                }
            }

            fun getNextPage(currentPage: SignUpPage): SignUpPage? {
                return when (currentPage) {
                    TermPage -> AccessRightsPage
                    AccessRightsPage -> AvoidAcquaintancesPage
                    AvoidAcquaintancesPage -> SignUpCompleted
                    else -> null
                }
            }
        }
    }
}
