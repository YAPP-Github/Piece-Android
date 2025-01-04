package com.puzzle.auth.graph.verification

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.verification.contract.AuthVerficationState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuthVerificationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthVerficationState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthVerficationState>(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<AuthVerificationViewModel, AuthVerficationState> {
        override fun create(state: AuthVerficationState): AuthVerificationViewModel
    }

    companion object :
        MavericksViewModelFactory<AuthVerificationViewModel, AuthVerficationState> by hiltMavericksViewModelFactory()
}
