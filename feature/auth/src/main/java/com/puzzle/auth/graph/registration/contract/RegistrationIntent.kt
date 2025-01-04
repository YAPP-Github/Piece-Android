package com.puzzle.auth.graph.registration.contract

import com.puzzle.navigation.NavigationEvent

sealed class RegistrationIntent {
    data object CheckAllTerms : RegistrationIntent()
    data object CheckPrivacyPolicy : RegistrationIntent()
    data object CheckTermsOfUse : RegistrationIntent()
    data class Navigate(val navigationEvent: NavigationEvent) : RegistrationIntent()
}
