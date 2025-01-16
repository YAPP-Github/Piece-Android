package com.puzzle.setting.graph.withdraw

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.navigation.NavigationHelper
import com.puzzle.setting.graph.withdraw.contract.WithdrawIntent
import com.puzzle.setting.graph.withdraw.contract.WithdrawSideEffect
import com.puzzle.setting.graph.withdraw.contract.WithdrawState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WithdrawViewModel @AssistedInject constructor(
    @Assisted initialState: WithdrawState,
    private val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<WithdrawState>(initialState) {
    private val intents = Channel<WithdrawIntent>(BUFFERED)
    private val sideEffects = Channel<WithdrawSideEffect>(BUFFERED)

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        sideEffects.receiveAsFlow()
            .onEach(::handleSideEffect)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: WithdrawIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    internal fun onSideEffect(sideEffect: WithdrawSideEffect) = viewModelScope.launch {
        sideEffects.send(sideEffect)
    }

    private fun processIntent(intent: WithdrawIntent) {
        when (intent) {
        }
    }

    private fun handleSideEffect(sideEffect: WithdrawSideEffect) {
        when (sideEffect) {
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WithdrawViewModel, WithdrawState> {
        override fun create(state: WithdrawState): WithdrawViewModel
    }

    companion object :
        MavericksViewModelFactory<WithdrawViewModel, WithdrawState> by hiltMavericksViewModelFactory()
}