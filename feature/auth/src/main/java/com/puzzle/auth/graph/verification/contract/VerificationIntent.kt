package com.puzzle.auth.graph.verification.contract

sealed class VerificationIntent {
    data class OnRequestAuthCodeClick(val phoneNumber: String) : VerificationIntent()
    data class OnVerifyClick(val phoneNumber: String, val code: String) : VerificationIntent()
    data object OnBackClick : VerificationIntent()
    data object OnNextClick : VerificationIntent()
}
