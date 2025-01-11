package com.puzzle.auth.graph.signup.contract

sealed class SignUpIntent {
    data object CheckAllTerms : SignUpIntent()
    data class CheckTerm(val termId: Int) : SignUpIntent()
    data class AgreeTerm(val termId: Int) : SignUpIntent()
    data object OnTermDetailClick : SignUpIntent()
    data object OnBackClick : SignUpIntent()
    data object OnNextClick : SignUpIntent()
}
