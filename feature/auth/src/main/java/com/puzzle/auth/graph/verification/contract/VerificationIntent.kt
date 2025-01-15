package com.puzzle.auth.graph.verification.contract

import com.puzzle.navigation.NavigationEvent

sealed class VerificationIntent {
    data class OnRequestAuthCodeClick(val phoneNumber: String) : VerificationIntent()
    data class OnVerifyClick(val code: String) : VerificationIntent()
    data class Navigate(val navigationEvent: NavigationEvent) : VerificationIntent()
}