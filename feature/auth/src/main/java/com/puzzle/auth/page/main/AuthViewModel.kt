package com.puzzle.auth.page.main

import com.airbnb.mvrx.MavericksViewModel
import com.puzzle.auth.page.main.contract.AuthState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AuthViewModel @AssistedInject constructor(
    @Assisted initialState: AuthState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthState>(initialState) {
}