package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.terms.Term

data class RegistrationState(
    val terms: List<Term> = emptyList(),
    val termsCheckedInfo: Map<Int, Boolean> = emptyMap(),
    val registrationPage: RegistrationPage = RegistrationPage.TermPage,
) : MavericksState {
    val allTermsAgreed = terms.all { termsCheckedInfo.getOrDefault(it.id, false) }

    enum class RegistrationPage {
        TermPage,
        TermDetailPage,
        AccessRightsPage,
        AvoidAcquaintancesPage,
        SignUpCompleted;

        companion object {
            fun getPreviousPage(currentPage: RegistrationPage): RegistrationPage {
                return when (currentPage) {
                    TermDetailPage -> TermPage
                    AccessRightsPage -> TermPage
                    AvoidAcquaintancesPage -> AccessRightsPage
                    else -> throw IllegalStateException("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: RegistrationPage): RegistrationPage {
                return when (currentPage) {
                    TermPage -> AccessRightsPage
                    AccessRightsPage -> AvoidAcquaintancesPage
                    AvoidAcquaintancesPage -> SignUpCompleted
                    else -> throw IllegalStateException("No previous page defined for $currentPage")
                }
            }
        }
    }
}
