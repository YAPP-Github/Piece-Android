package com.puzzle.matching.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.common.event.SnackBarType
import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.domain.model.error.HttpResponseStatus
import com.puzzle.domain.model.match.MatchStatus
import com.puzzle.domain.model.match.getRemainingTimeInSec
import com.puzzle.domain.model.user.ProfileStatus
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.repository.ConfigureRepository
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.matching.graph.main.contract.MatchingIntent
import com.puzzle.matching.graph.main.contract.MatchingState
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent.To
import com.puzzle.navigation.NavigationHelper
import com.puzzle.navigation.ProfileGraphDest
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MatchingViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingState,
    private val configureRepository: ConfigureRepository,
    private val matchingRepository: MatchingRepository,
    private val userRepository: UserRepository,
    private val timer: Timer,
    private val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
    internal val navigationHelper: NavigationHelper,
) : MavericksViewModel<MatchingState>(initialState) {
    private val intents = Channel<MatchingIntent>(BUFFERED)

    private var timerJob: Job? = null

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        viewModelScope.launch {
            configureRepository.isNotificationEnabled()
                .onSuccess { setState { copy(isNotificationEnabled = it) } }
                .onFailure { errorHelper.sendError(it) }
        }
    }

    internal fun onIntent(intent: MatchingIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: MatchingIntent) {
        when (intent) {
            MatchingIntent.OnButtonClick -> processOnButtonClick()
            is MatchingIntent.OnMatchingDetailClick -> withState {
                navigationHelper.navigate(
                    To(
                        MatchingGraphDest.MatchingDetailRoute(it.matchInfo!!.matchId)
                    )
                )
            }

            MatchingIntent.OnEditProfileClick -> moveToProfileRegisterScreen()
            MatchingIntent.OnCheckMyProfileClick -> navigationHelper.navigate(
                To(MatchingGraphDest.ProfilePreviewRoute)
            )
        }
    }

    internal fun initMatchInfo() = viewModelScope.launch {
        userRepository.getUserInfo()
            .onSuccess { userInfo ->
                setState { copy(userRole = userInfo.userRole) }

                when {
                    userInfo.profileStatus == ProfileStatus.REJECTED -> getRejectReason()
                    userInfo.userRole == UserRole.USER -> getMatchInfo()
                    else -> Unit
                }

                setState { copy(isLoading = false) }

            }.onFailure { errorHelper.sendError(it) }

    }

    private fun moveToProfileRegisterScreen() {
        navigationHelper.navigate(To(ProfileGraphDest.RegisterProfileRoute))
    }

    private suspend fun getRejectReason() {
        userRepository.getRejectReason()
            .onSuccess {
                setState {
                    copy(
                        isImageRejected = it.reasonImage,
                        isDescriptionRejected = it.reasonValues
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
    }

    private suspend fun getMatchInfo() = matchingRepository.getMatchInfo()
        .onSuccess {
            setState { copy(matchInfo = it) }

            when (it.matchStatus) {
                MatchStatus.REFUSED -> startWaitingTimer()
                MatchStatus.BEFORE_OPEN -> {
                    eventHelper.sendEvent(
                        PieceEvent.ShowSnackBar(
                            msg = "새로운 매칭 조각이 도착했어요",
                            type = SnackBarType.Matching
                        )
                    )
                    startMatchingValidTimer(startTimeInSec = it.remainMatchingUpdateTimeInSec)
                }

                else -> startMatchingValidTimer(startTimeInSec = it.remainMatchingUpdateTimeInSec)
            }

            // MatchingHome 화면에서 사전에 MatchingDetail에서 필요한 데이터를 케싱해놓습니다.
            matchingRepository.loadOpponentProfile()
                .onFailure { errorHelper.sendError(it) }
        }.onFailure {
            if (it is HttpResponseException) {
                // 1. 회원가입하고 처음 매칭을 하는데 아직 오후 10시가 안되었을 때
                // 2. 내가 차단했을 때
                // 3. 상대방 아이디가 없어졌을 때
                if (it.status == HttpResponseStatus.NotFound) {
                    startWaitingTimer()
                }
                return@onFailure
            }

            errorHelper.sendError(it)
        }

    private fun processOnButtonClick() = withState { state ->
        when (state.matchInfo?.matchStatus) {
            MatchStatus.BEFORE_OPEN -> checkMatchingPiece()
            MatchStatus.WAITING -> acceptMatchingInResponsed()
            MatchStatus.GREEN_LIGHT -> acceptMatchingInMatced()
            MatchStatus.MATCHED -> navigateToContactScreen()
            else -> Unit
        }
    }

    private fun navigateToContactScreen() =
        navigationHelper.navigate(To(MatchingGraphDest.ContactRoute))

    private fun checkMatchingPiece() = withState {
        viewModelScope.launch {
            matchingRepository.checkMatchingPiece()
                .onSuccess {
                    setState { copy(matchInfo = matchInfo?.copy(matchStatus = MatchStatus.WAITING)) }
                }.onFailure { errorHelper.sendError(it) }

            navigationHelper.navigate(
                To(MatchingGraphDest.MatchingDetailRoute(it.matchInfo!!.matchId))
            )
        }
    }

    private fun acceptMatchingInResponsed() = viewModelScope.launch {
        matchingRepository.acceptMatching()
            .onSuccess {
                setState { copy(matchInfo = matchInfo?.copy(matchStatus = MatchStatus.RESPONDED)) }

                eventHelper.sendEvent(
                    PieceEvent.ShowSnackBar(msg = "매칭을 수락했습니다", type = SnackBarType.Matching)
                )
            }.onFailure { errorHelper.sendError(it) }
    }

    private fun acceptMatchingInMatced() = viewModelScope.launch {
        matchingRepository.acceptMatching()
            .onSuccess {
                setState { copy(matchInfo = matchInfo?.copy(matchStatus = MatchStatus.MATCHED)) }
            }.onFailure { errorHelper.sendError(it) }
    }

    private fun startWaitingTimer() {
        timerJob?.cancel()

        val currentTimeInSec = (System.currentTimeMillis() / 1000L)
        timerJob = viewModelScope.launch {
            timer.startTimer(getRemainingTimeInSec(currentTimeInSec))
                .collect { remainTimeInSec ->
                    setState { copy(remainWaitingTimeInSec = remainTimeInSec) }

                    if (remainTimeInSec == 0L) {
                        getMatchInfo()

                        timerJob?.cancel()
                    }
                }
        }
    }

    private fun startMatchingValidTimer(startTimeInSec: Long) {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            timer.startTimer(getRemainingTimeInSec(startTimeInSec))
                .collect { remainTimeInSec ->
                    setState {
                        if (matchInfo != null) {
                            copy(matchInfo = matchInfo.copy(remainMatchingUpdateTimeInSec = remainTimeInSec))
                        } else {
                            this
                        }
                    }

                    if (remainTimeInSec == 0L) {
                        getMatchInfo()

                        timerJob?.cancel()
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingViewModel, MatchingState> {
        override fun create(state: MatchingState): MatchingViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingViewModel, MatchingState> by hiltMavericksViewModelFactory()
}
