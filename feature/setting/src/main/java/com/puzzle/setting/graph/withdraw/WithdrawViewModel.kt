package com.puzzle.setting.graph.withdraw

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.setting.graph.withdraw.contract.WithdrawIntent
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
    private val authRepository: AuthRepository,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<WithdrawState>(initialState) {
    private val _intents = Channel<WithdrawIntent>(BUFFERED)

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
            is WithdrawIntent.OnReasonsClick -> selectReason(intent.withdrawReason)
            is WithdrawIntent.UpdateReason -> updateReason(intent.reason)
            WithdrawIntent.OnNextClick -> moveToWithdrawPage()
            WithdrawIntent.OnWithdrawClick -> withdraw()
            WithdrawIntent.onBackClick -> moveToPreviousScreen()
        }
    }

    private fun updateReason(reason: String) {
        setState { copy(reason = reason) }
    }

    private suspend fun moveToPreviousScreen() {
        navigationHelper.navigate(NavigationEvent.Up)
    }

    private fun moveToWithdrawPage() {
        setState {
            copy(currentPage = WithdrawState.WithdrawPage.getNextPage(currentPage))
        }
    }

    private fun withdraw() = withState { state ->
        viewModelScope.launch {
            val reason = when (state.selectedReason) {
                WithdrawState.WithdrawReason.Other -> state.reason
                else -> state.selectedReason?.label ?: ""
            }

            authRepository.withdraw(reason)
                .onSuccess { navigationHelper.navigate(NavigationEvent.TopLevelTo(AuthGraph)) }
                .onFailure { errorHelper.sendError(it) }
        }
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

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WithdrawViewModel, WithdrawState> {
        override fun create(state: WithdrawState): WithdrawViewModel
    }

    companion object :
        MavericksViewModelFactory<WithdrawViewModel, WithdrawState> by hiltMavericksViewModelFactory()
}
