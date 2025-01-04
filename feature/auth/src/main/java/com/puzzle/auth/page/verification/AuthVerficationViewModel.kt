package com.puzzle.auth.page.verification

import com.airbnb.mvrx.MavericksViewModel
import com.puzzle.auth.page.verification.contract.AuthVerficationState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AuthVerficationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthVerficationState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthVerficationState>(initialState) {
}
