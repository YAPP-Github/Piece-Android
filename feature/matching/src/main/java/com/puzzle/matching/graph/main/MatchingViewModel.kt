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
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.matching.graph.main.contract.MatchingIntent
import com.puzzle.matching.graph.main.contract.MatchingSideEffect
import com.puzzle.matching.graph.main.contract.MatchingState
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.navigation.ProfileGraphDest
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
    private val matchingRepository: MatchingRepository,
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
    internal val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<MatchingState>(initialState) {
    private val intents = Channel<MatchingIntent>(BUFFERED)
    private val _sideEffects = Channel<MatchingSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
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
            MatchingIntent.OnEditProfileClick -> moveToProfileRegisterScreen()
        }
    }

    internal fun initMatchInfo() = viewModelScope.launch {
        userRepository.getUserRole()
            .onSuccess { userRole ->
                if (userRole == UserRole.USER) {
                    getMatchInfo()
                }

                setState { copy(userRole = userRole) }

                // MatchingHome 화면에서 사전에 내 프로필 데이터를 케싱해놓습니다.
                loadMyProfile()
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun moveToProfileRegisterScreen() {
        navigationHelper.navigate(NavigationEvent.NavigateTo(ProfileGraphDest.RegisterProfileRoute))
    }

    private fun loadMyProfile() = viewModelScope.launch {
        val profileBasicJob = launch {
            profileRepository.loadMyProfileBasic()
                .onFailure { errorHelper.sendError(it) }
        }
        val valueTalksJob = launch {
            profileRepository.loadMyValuePicks()
                .onFailure { errorHelper.sendError(it) }
        }
        val valuePicksJob = launch {
            profileRepository.loadMyValueTalks()
                .onFailure { errorHelper.sendError(it) }
        }

        profileBasicJob.join()
        valueTalksJob.join()
        valuePicksJob.join()
    }

    private suspend fun getMatchInfo() = matchingRepository.getMatchInfo()
        .onSuccess {
            setState { copy(matchInfo = it) }

            // MatchingHome 화면에서 사전에 MatchingDetail에서 필요한 데이터를 케싱해놓습니다.
            matchingRepository.loadOpponentProfile()
                .onFailure { errorHelper.sendError(it) }
        }
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
