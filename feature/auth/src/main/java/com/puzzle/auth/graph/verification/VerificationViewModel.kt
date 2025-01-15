package com.puzzle.auth.graph.verification

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.verification.contract.VerificationIntent
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
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

class VerificationViewModel @AssistedInject constructor(
    @Assisted initialState: VerificationState,
    private val navigationHelper: NavigationHelper,
    private val authRepository: AuthRepository,
    private val timer: Timer,
) : MavericksViewModel<VerificationState>(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<VerificationViewModel, VerificationState> {
        override fun create(state: VerificationState): VerificationViewModel
    }

    private val intents = Channel<VerificationIntent>(BUFFERED)
    private val sideEffects = Channel<VerificationSideEffect>(BUFFERED)
    private var timerJob: Job? = null

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        sideEffects.receiveAsFlow()
            .onEach(::handleSideEffect)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: VerificationIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    internal fun onSideEffect(sideEffect: VerificationSideEffect) = viewModelScope.launch {
        sideEffects.send(sideEffect)
    }

    private fun processIntent(intent: VerificationIntent) {
        when (intent) {
            is VerificationIntent.OnRequestAuthCodeClick -> requestAuthCode(intent.phoneNumber)
            is VerificationIntent.OnVerifyClick -> verifyAuthCode(intent.code)
        }
    }

    private fun handleSideEffect(sideEffect: VerificationSideEffect) {
        when (sideEffect) {
            is VerificationSideEffect.Navigate -> navigationHelper.navigate(sideEffect.navigationEvent)
        }
    }

    private fun requestAuthCode(phoneNumber: String) {
        viewModelScope.launch {
            authRepository.requestAuthCode(phoneNumber).fold(
                onSuccess = {
                    setState {
                        copy(
                            isValidPhoneNumber = true,
                            isAuthCodeRequested = true,
                            authCodeStatus = VerificationState.AuthCodeStatus.INIT,
                        )
                    }
                    timer = Timer()
                    startTimer()
                },
                onFailure = {
                    setState {
                        copy(isValidPhoneNumber = false)
                    }
                },
            )
        }
    }

    private fun verifyAuthCode(code: String) {
        pauseTimer()
        viewModelScope.launch {
            authRepository.verifyAuthCode(code).fold(
                onSuccess = {
                    stopTimer()

                    setState {
                        copy(
                            _remainingTimeInSec = 0,
                            authCodeStatus = VerificationState.AuthCodeStatus.VERIFIED,
                            isVerified = true,
                        )
                    }

                    navigationHelper.navigate(
                        NavigationEvent.NavigateTo(
                            route = AuthGraphDest.SignUpRoute,
                            popUpTo = AuthGraph,
                        )
                    )
                },
                onFailure = {
                    startTimer()

                    setState {
                        copy(authCodeStatus = VerificationState.AuthCodeStatus.INVALID)
                    }
                },
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            timer.startTimer()
                .collect { remaining ->
                    setState {
                        copy(_remainingTimeInSec = remaining)
                    }
                    timer = Timer(remaining)
                    if (remaining == 0) {
                        setState {
                            copy(authCodeStatus = VerificationState.AuthCodeStatus.TIME_EXPIRED)
                        }
                        timerJob?.cancel()
                    }
                }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun stopTimer() {
        timerJob?.cancel()
        setState {
            copy(
                _remainingTimeInSec = 0,
                authCodeStatus = VerificationState.AuthCodeStatus.INIT,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object :
        MavericksViewModelFactory<VerificationViewModel, VerificationState> by hiltMavericksViewModelFactory()
}
