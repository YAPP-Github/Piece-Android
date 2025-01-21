package com.puzzle.setting.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.navigation.NavigationEvent.NavigateTo
import com.puzzle.navigation.NavigationHelper
import com.puzzle.navigation.SettingGraphDest
import com.puzzle.setting.graph.main.contract.SettingIntent
import com.puzzle.setting.graph.main.contract.SettingSideEffect
import com.puzzle.setting.graph.main.contract.SettingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingViewModel @AssistedInject constructor(
    @Assisted initialState: SettingState,
    private val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<SettingState>(initialState) {
    private val intents = Channel<SettingIntent>(BUFFERED)
    private val sideEffects = Channel<SettingSideEffect>(BUFFERED)

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        sideEffects.receiveAsFlow()
            .onEach(::handleSideEffect)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: SettingIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    internal fun onSideEffect(sideEffect: SettingSideEffect) = viewModelScope.launch {
        sideEffects.send(sideEffect)
    }

    private fun processIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.OnWithdrawClick -> moveToWithdrawScreen()
        }
    }

    // TODO side effect 처리
    private fun moveToWithdrawScreen() {
        navigationHelper.navigate(NavigateTo(SettingGraphDest.WithdrawRoute))
    }

    private fun handleSideEffect(sideEffect: SettingSideEffect) {
        when (sideEffect) {
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SettingViewModel, SettingState> {
        override fun create(state: SettingState): SettingViewModel
    }

    companion object :
        MavericksViewModelFactory<SettingViewModel, SettingState> by hiltMavericksViewModelFactory()
}