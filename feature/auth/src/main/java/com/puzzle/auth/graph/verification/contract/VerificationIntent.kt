package com.puzzle.auth.graph.verification.contract

sealed class VerificationIntent {
    data object RequestVerificationCode : VerificationIntent()
    data class VerifyCode(val code: String) : VerificationIntent()
    data object CompleteVerification : VerificationIntent()
}