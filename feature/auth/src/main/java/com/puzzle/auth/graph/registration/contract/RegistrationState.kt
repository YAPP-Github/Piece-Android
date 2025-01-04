package com.puzzle.auth.graph.registration.contract

import com.airbnb.mvrx.MavericksState

data class RegistrationState(
    val isTermsOfUseChecked: Boolean = false,
    val isPrivacyPolicyChecked: Boolean = false,
) : MavericksState {
    val agreeAllTerms = isTermsOfUseChecked && isPrivacyPolicyChecked
}
