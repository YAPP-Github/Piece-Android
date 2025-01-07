package com.puzzle.auth.graph.registration.contract

sealed class RegistrationIntent {
    data object CheckAllTerms : RegistrationIntent()
    data class CheckTerm(val termId: Int) : RegistrationIntent()
}
