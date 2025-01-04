package com.puzzle.auth.page.registration

import com.airbnb.mvrx.MavericksViewModel
import com.puzzle.auth.page.registration.contract.AuthRegistrationState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AuthRegistrationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthRegistrationState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthRegistrationState>(initialState) {
}