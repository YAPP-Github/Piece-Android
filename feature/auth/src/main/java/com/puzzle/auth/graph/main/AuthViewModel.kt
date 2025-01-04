package com.puzzle.auth.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.main.contract.AuthIntent
import com.puzzle.auth.graph.main.contract.AuthSideEffect
import com.puzzle.auth.graph.main.contract.AuthState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel @AssistedInject constructor(
    @Assisted initialState: AuthState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<AuthState>(initialState) {
    private val intents = Channel<AuthIntent>(BUFFERED)

    private val _sideEffect = Channel<AuthSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: AuthIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Navigate -> navigationHelper.navigate(intent.navigationEvent)
        }
    }

    private fun handleSideEffect(sideEffect: AuthSideEffect) {
        when (sideEffect) {
            else -> Unit
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<AuthViewModel, AuthState> {
        override fun create(state: AuthState): AuthViewModel
    }

    companion object :
        MavericksViewModelFactory<AuthViewModel, AuthState> by hiltMavericksViewModelFactory()
}