package com.puzzle.auth.graph.verification

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.verification.contract.VerificationIntent
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.domain.repository.VerificationCodeRepository
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class VerificationViewModel @AssistedInject constructor(
    @Assisted initialState: VerificationState,
    private val navigationHelper: NavigationHelper,
    private val verificationCodeRepository: VerificationCodeRepository,
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
            is VerificationIntent.OnRequestVerificationCodeClick -> requestVerificationCode(intent.phoneNumber)
            is VerificationIntent.OnVerifyClick -> verify(intent.code)
            VerificationIntent.OnNextClick -> moveToNextPage()
        }
    }

    private fun handleSideEffect(sideEffect: VerificationSideEffect) {
        when (sideEffect) {
            is VerificationSideEffect.Navigate -> navigationHelper.navigate(sideEffect.navigationEvent)
        }
    }

    private fun moveToNextPage() {
        navigationHelper.navigate(
            NavigationEvent.NavigateTo(
                route = AuthGraphDest.RegistrationRoute,
                popUpTo = AuthGraph,
            )
        )
    }

    private fun requestVerificationCode(phoneNumber: String) {
        viewModelScope.launch {
            val result = verificationCodeRepository.requestVerificationCode(phoneNumber)

            setState {
                copy(
                    isValidPhoneNumber = result
                )
            }

            if (!result) return@launch

            startCodeExpiryTimer(5)
        }
    }

    private fun verify(code: String) {
        viewModelScope.launch {
            val result = verificationCodeRepository.verify(code)
            setState {
                copy(
                    isVerified = result,
                    remainingTimeInSec = 0,
                    verificationCodeStatus = if (result) {
                        timerJob?.cancel()
                        VerificationState.VerificationCodeStatus.VERIFIED
                    } else {
                        VerificationState.VerificationCodeStatus.INVALID
                    },
                )
            }
        }
    }

    private fun startCodeExpiryTimer(durationInSec: Int = 300) {
        timerJob?.cancel()

        setState {
            copy(
                hasStarted = true,
                remainingTimeInSec = durationInSec,
                verificationCodeStatus = VerificationState.VerificationCodeStatus.DO_NOT_SHARE,
            )
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                withState { currentState ->
                    if (currentState.remainingTimeInSec <= 0) {
                        setState {
                            copy(
                                remainingTimeInSec = 0,
                                verificationCodeStatus = VerificationState.VerificationCodeStatus.TIME_EXPIRED,
                            )
                        }
                        return@withState
                    }

                    setState {
                        copy(remainingTimeInSec = currentState.remainingTimeInSec - 1)
                    }
                }
            }
        }
    }

    companion object :
        MavericksViewModelFactory<VerificationViewModel, VerificationState> by hiltMavericksViewModelFactory()
}
