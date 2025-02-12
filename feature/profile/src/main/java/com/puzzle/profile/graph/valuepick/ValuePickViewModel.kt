package com.puzzle.profile.graph.valuepick

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.usecase.profile.GetMyValuePicksUseCase
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.valuepick.contract.ValuePickIntent
import com.puzzle.profile.graph.valuepick.contract.ValuePickState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ValuePickViewModel @AssistedInject constructor(
    @Assisted initialState: ValuePickState,
    private val getMyValuePicksUseCase: GetMyValuePicksUseCase,
    private val profileRepository: ProfileRepository,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<ValuePickState>(initialState) {
    private val intents = Channel<ValuePickIntent>(BUFFERED)

    init {
        initValuePick()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    private fun initValuePick() = viewModelScope.launch {
        getMyValuePicksUseCase().onSuccess {
            setState { copy(valuePicks = it) }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun onIntent(intent: ValuePickIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: ValuePickIntent) {
        when (intent) {
            is ValuePickIntent.OnUpdateClick -> updateValuePicks(intent.newValuePicks)
            ValuePickIntent.OnEditClick -> setEditMode()
            ValuePickIntent.OnBackClick -> processOnBackClick()
        }
    }

    private fun setEditMode() = setState { copy(screenState = ValuePickState.ScreenState.EDITING) }

    private fun updateValuePicks(valuePicks: List<MyValuePick>) = viewModelScope.launch {
        profileRepository.updateMyValuePicks(valuePicks)
            .onSuccess {
                setState {
                    copy(
                        valuePicks = it,
                        screenState = ValuePickState.ScreenState.NORMAL,
                    )
                }
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun processOnBackClick() = withState { state ->
        when (state.screenState) {
            ValuePickState.ScreenState.EDITING -> setState { copy(screenState = ValuePickState.ScreenState.NORMAL) }
            ValuePickState.ScreenState.NORMAL -> navigationHelper.navigate(
                NavigationEvent.NavigateUp
            )
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ValuePickViewModel, ValuePickState> {
        override fun create(state: ValuePickState): ValuePickViewModel
    }

    companion object :
        MavericksViewModelFactory<ValuePickViewModel, ValuePickState> by hiltMavericksViewModelFactory()

}
