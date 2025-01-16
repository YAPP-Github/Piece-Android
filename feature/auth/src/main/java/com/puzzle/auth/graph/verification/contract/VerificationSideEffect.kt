package com.puzzle.auth.graph.verification.contract

import com.puzzle.navigation.NavigationEvent

sealed class VerificationSideEffect {
    data class RequestAuthCode(val phoneNumber: String) : VerificationSideEffect()
    data class VerifyAuthCode(val phoneNumber: String, val code: String) : VerificationSideEffect()
    data class Navigate(val navigationEvent: NavigationEvent) : VerificationSideEffect()
}
