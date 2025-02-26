package com.puzzle.profile.graph.valuetalk

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.usecase.profile.GetMyValueTalksUseCase
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkIntent
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
    private val profileRepository: ProfileRepository,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<ValueTalkState>(initialState) {

    private val intents = Channel<ValueTalkIntent>(BUFFERED)

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

        profileRepository.aiSummary
            .collect { response ->
                setState {
                    copy(
                        valueTalks = valueTalks.map { valueTalk ->
                            if (valueTalk.id == response.id) valueTalk.copy(summary = response.summary)
                            else valueTalk
                        }.toList()
                    )
                }
            }
    }

    internal fun connectSse() {
        viewModelScope.launch {
            profileRepository.connectSSE()
                .onFailure { errorHelper.sendError(it) }
        }
    }

    internal fun disconnectSse() {
        viewModelScope.launch {
            profileRepository.disconnectSSE()
                .onFailure { errorHelper.sendError(it) }
        }
    }

    internal fun onIntent(intent: ValueTalkIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: ValueTalkIntent) {
        when (intent) {
            ValueTalkIntent.OnBackClick -> processBackClick()
            ValueTalkIntent.OnEditClick -> setEditMode()
            is ValueTalkIntent.OnUpdateClick -> updateValueTalk(intent.newValueTalks)
            is ValueTalkIntent.OnAiSummarySaveClick -> updateAiSummaryClick(intent.newValueTalk)
        }
    }

    private fun processBackClick() = withState { state ->
        when (state.screenState) {
            ValueTalkState.ScreenState.NORMAL -> navigationHelper.navigate(NavigationEvent.Up)
            ValueTalkState.ScreenState.EDITING ->
                setState { copy(screenState = ValueTalkState.ScreenState.NORMAL) }
        }
    }

    private fun setEditMode() = setState { copy(screenState = ValueTalkState.ScreenState.EDITING) }

    private fun updateValueTalk(valueTalks: List<MyValueTalk>) = viewModelScope.launch {
        profileRepository.updateMyValueTalks(valueTalks)
            .onSuccess {
                setState {
                    copy(
                        screenState = ValueTalkState.ScreenState.NORMAL,
                        valueTalks = valueTalks.map { it.copy(summary = "") }
                    )
                }
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun updateAiSummaryClick(valueTalk: MyValueTalk) = viewModelScope.launch {
        profileRepository.updateAiSummary(
            profileTalkId = valueTalk.id,
            summary = valueTalk.summary,
        ).onSuccess {
            setState {
                val newValueTalks = valueTalks.map {
                    if (it.id == valueTalk.id) valueTalk
                    else it
                }

                copy(
                    screenState = ValueTalkState.ScreenState.NORMAL,
                    valueTalks = newValueTalks,
                )
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ValueTalkViewModel, ValueTalkState> {
        override fun create(state: ValueTalkState): ValueTalkViewModel
    }

    companion object :
        MavericksViewModelFactory<ValueTalkViewModel, ValueTalkState> by hiltMavericksViewModelFactory()

}
