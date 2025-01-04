package com.puzzle.auth.graph.registration

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.registration.contract.AuthRegistrationState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuthRegistrationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthRegistrationState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthRegistrationState>(initialState) {

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<AuthRegistrationViewModel, AuthRegistrationState> {
        override fun create(state: AuthRegistrationState): AuthRegistrationViewModel
    }

    companion object :
        MavericksViewModelFactory<AuthRegistrationViewModel, AuthRegistrationState> by hiltMavericksViewModelFactory()
}