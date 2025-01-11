package com.puzzle.auth.graph.verification.contract

sealed class VerificationIntent {
    data class OnRequestVerificationCodeClick(val phoneNumber: String) : VerificationIntent()
    data class OnVerifyClick(val code: String) : VerificationIntent()
    data object OnNextClick : VerificationIntent()
}