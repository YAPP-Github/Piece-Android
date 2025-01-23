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
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<SettingState>(initialState) {
    private val _intents = Channel<SettingIntent>(BUFFERED)

    private val _sideEffects = Channel<SettingSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: SettingIntent) = viewModelScope.launch {
        _intents.send(intent)
    }

    private suspend fun processIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.OnWithdrawClick ->
                _sideEffects.send(SettingSideEffect.Navigate(NavigateTo(SettingGraphDest.WithdrawRoute)))
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SettingViewModel, SettingState> {
        override fun create(state: SettingState): SettingViewModel
    }

    companion object :
        MavericksViewModelFactory<SettingViewModel, SettingState> by hiltMavericksViewModelFactory()
}