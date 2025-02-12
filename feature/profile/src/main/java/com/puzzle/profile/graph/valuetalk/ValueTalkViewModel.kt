package com.puzzle.profile.graph.valuetalk

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.usecase.profile.GetMyValueTalksUseCase
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkIntent
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkSideEffect
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ValueTalkViewModel @AssistedInject constructor(
    @Assisted initialState: ValueTalkState,
    private val getMyValueTalksUseCase: GetMyValueTalksUseCase,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<ValueTalkState>(initialState) {

    private val intents = Channel<ValueTalkIntent>(BUFFERED)
    private val _sideEffects = Channel<ValueTalkSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        initValueTalk()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    private fun initValueTalk() = viewModelScope.launch {
        getMyValueTalksUseCase().onSuccess {
            setState { copy(valueTalks = it) }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun onIntent(intent: ValueTalkIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: ValueTalkIntent) {
        when (intent) {
            ValueTalkIntent.OnBackClick -> _sideEffects.send(
                ValueTalkSideEffect.Navigate(NavigationEvent.NavigateUp)
            )
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ValueTalkViewModel, ValueTalkState> {
        override fun create(state: ValueTalkState): ValueTalkViewModel
    }

    companion object :
        MavericksViewModelFactory<ValueTalkViewModel, ValueTalkState> by hiltMavericksViewModelFactory()

}
