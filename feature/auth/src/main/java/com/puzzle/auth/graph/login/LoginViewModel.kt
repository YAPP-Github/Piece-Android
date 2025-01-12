package com.puzzle.auth.graph.login

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.login.contract.LoginIntent
import com.puzzle.auth.graph.login.contract.LoginSideEffect
import com.puzzle.auth.graph.login.contract.LoginState
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

class LoginViewModel @AssistedInject constructor(
    @Assisted initialState: LoginState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<LoginState>(initialState) {
    private val intents = Channel<LoginIntent>(BUFFERED)

    private val _sideEffects = Channel<LoginSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: LoginIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Navigate -> navigationHelper.navigate(intent.navigationEvent)
        }
    }

    internal fun onSideEffect(sideEffect: LoginSideEffect) = viewModelScope.launch {
        _sideEffects.send(sideEffect)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<LoginViewModel, LoginState> {
        override fun create(state: LoginState): LoginViewModel
    }

    companion object :
        MavericksViewModelFactory<LoginViewModel, LoginState> by hiltMavericksViewModelFactory()
}