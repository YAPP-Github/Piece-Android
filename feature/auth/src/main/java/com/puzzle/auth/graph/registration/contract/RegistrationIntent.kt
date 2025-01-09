package com.puzzle.auth.graph.registration.contract

sealed class RegistrationIntent {
    data object CheckAllTerms : RegistrationIntent()
    data class CheckTerm(val termId: Int) : RegistrationIntent()
    data class AgreeTerm(val termId: Int) : RegistrationIntent()
    data object OnTermDetailClick : RegistrationIntent()
    data object OnBackClick : RegistrationIntent()
    data object OnNextClick : RegistrationIntent()
}
