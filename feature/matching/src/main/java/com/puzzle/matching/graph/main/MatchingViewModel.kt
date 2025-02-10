package com.puzzle.matching.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.match.MatchStatus
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.matching.graph.main.contract.MatchingIntent
import com.puzzle.matching.graph.main.contract.MatchingSideEffect
import com.puzzle.matching.graph.main.contract.MatchingState
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

class MatchingViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingState,
    private val navigationHelper: NavigationHelper,
    private val matchingRepository: MatchingRepository,
    private val userRepository: UserRepository,
    internal val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<MatchingState>(initialState) {
    private val intents = Channel<MatchingIntent>(BUFFERED)
    private val _sideEffects = Channel<MatchingSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        initMatchInfo()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: MatchingIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: MatchingIntent) {
        when (intent) {
            MatchingIntent.OnButtonClick -> processOnButtonClick()
            is MatchingIntent.Navigate -> navigationHelper.navigate(intent.navigationEvent)
        }
    }

    private fun initMatchInfo() = viewModelScope.launch {
        userRepository.getUserRole()
            .onSuccess { userRole ->
                if (userRole == UserRole.USER) {
                    getMatchInfo()
                }

                setState { copy(userRole = userRole) }
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private suspend fun getMatchInfo() = matchingRepository.getMatchInfo()
        .onSuccess { setState { copy(matchInfo = it) } }
        .onFailure { errorHelper.sendError(it) }

    private fun processOnButtonClick() = withState { state ->
        when (state.matchInfo?.matchStatus) {
            MatchStatus.BEFORE_OPEN -> checkMatchingPiece()
            MatchStatus.GREEN_LIGHT -> acceptMatching()
            MatchStatus.MATCHED -> {
                // Todo 연락처 공개 페이지로 이동
                // navigationHelper.navigate()
            }

            else -> Unit
        }
    }

    private fun checkMatchingPiece() = viewModelScope.launch {
        matchingRepository.checkMatchingPiece()
            .onSuccess {
                setState { copy(matchInfo = matchInfo?.copy(matchStatus = MatchStatus.RESPONDED)) }
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun acceptMatching() = viewModelScope.launch {
        matchingRepository.acceptMatching()
            .onSuccess {
                setState { copy(matchInfo = matchInfo?.copy(matchStatus = MatchStatus.MATCHED)) }
            }
            .onFailure { errorHelper.sendError(it) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingViewModel, MatchingState> {
        override fun create(state: MatchingState): MatchingViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingViewModel, MatchingState> by hiltMavericksViewModelFactory()
}
