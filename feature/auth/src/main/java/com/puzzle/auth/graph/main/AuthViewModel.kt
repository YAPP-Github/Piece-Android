package com.puzzle.auth.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.main.contract.AuthState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuthViewModel @AssistedInject constructor(
    @Assisted initialState: AuthState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthState>(initialState) {

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<AuthViewModel, AuthState> {
        override fun create(state: AuthState): AuthViewModel
    }

    companion object :
        MavericksViewModelFactory<AuthViewModel, AuthState> by hiltMavericksViewModelFactory()
}