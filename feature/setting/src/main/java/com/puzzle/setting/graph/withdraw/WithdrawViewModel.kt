package com.puzzle.setting.graph.withdraw

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.NavigationEvent
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
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<WithdrawState>(initialState) {
    private val _intents = Channel<WithdrawIntent>(BUFFERED)

    private val _sideEffects = Channel<WithdrawSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: WithdrawIntent) = viewModelScope.launch {
        _intents.send(intent)
    }

    private suspend fun processIntent(intent: WithdrawIntent) {
        when (intent) {
            WithdrawIntent.OnSameReasonClick -> selectNothing()
            is WithdrawIntent.OnReasonsClick -> selectReason(intent.withdrawReason)
            WithdrawIntent.OnNextClick -> moveToWithdrawPage()
            WithdrawIntent.OnWithdrawClick -> withdraw()
            WithdrawIntent.onBackClick -> moveToPreviousScreen()
        }
    }

    private suspend fun moveToPreviousScreen() {
        _sideEffects.send(WithdrawSideEffect.Navigate(NavigationEvent.NavigateUp))
    }

    private fun moveToWithdrawPage() {
        setState {
            copy(currentPage = WithdrawState.WithdrawPage.getNextPage(currentPage))
        }
    }

    private suspend fun withdraw() {
        _sideEffects.send(WithdrawSideEffect.Navigate(NavigationEvent.TopLevelNavigateTo(AuthGraph)))
    }

    private fun selectReason(reason: WithdrawState.WithdrawReason) {
        setState {
            val selectedReason =
                if (this.selectedReason == reason) {
                    null
                } else {
                    reason
                }

            copy(selectedReason = selectedReason)
        }
    }

    private fun selectNothing() {
        setState {
            copy(selectedReason = null)
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WithdrawViewModel, WithdrawState> {
        override fun create(state: WithdrawState): WithdrawViewModel
    }

    companion object :
        MavericksViewModelFactory<WithdrawViewModel, WithdrawState> by hiltMavericksViewModelFactory()
}