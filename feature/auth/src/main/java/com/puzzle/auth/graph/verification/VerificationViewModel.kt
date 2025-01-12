package com.puzzle.auth.graph.verification

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.verification.contract.VerificationIntent
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.domain.usecase.RequestAuthCodeUseCase
import com.puzzle.domain.usecase.VerifyAuthCodeUseCase
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent
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

class VerificationViewModel @AssistedInject constructor(
    @Assisted initialState: VerificationState,
    private val navigationHelper: NavigationHelper,
    private val requestAuthCodeUseCase: RequestAuthCodeUseCase,
    private val verifyAuthCodeUseCase: VerifyAuthCodeUseCase,
) : MavericksViewModel<VerificationState>(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<VerificationViewModel, VerificationState> {
        override fun create(state: VerificationState): VerificationViewModel
    }

    private val intents = Channel<VerificationIntent>(BUFFERED)
    private val sideEffects = Channel<VerificationSideEffect>(BUFFERED)

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

    private fun requestAuthCode(phoneNumber: String) {
        viewModelScope.launch {
            requestAuthCodeUseCase(
                phoneNumber = phoneNumber,
                object : RequestAuthCodeUseCase.Callback {
                    override fun onRequestSuccess() {
                        setState {
                            copy(
                                isValidPhoneNumber = true,
                                isAuthCodeRequested = true,
                                authCodeStatus = VerificationState.AuthCodeStatus.INIT,
                            )
                        }
                    }

                    override fun onRequestFail(e: Throwable) {
                        setState {
                            copy(
                                authCodeStatus = VerificationState.AuthCodeStatus.INVALID,
                            )
                        }
                    }

                    override fun onTimeExpired() {
                        setState {
                            copy(
                                authCodeStatus = VerificationState.AuthCodeStatus.TIME_EXPIRED,
                                remainingTimeInSec = 0,
                            )
                        }
                    }

                    override fun onTick(remainingTimeInSec: Int) {
                        setState {
                            copy(
                                remainingTimeInSec = remainingTimeInSec,
                            )
                        }
                    }

                }
            )
        }
    }

    private fun verifyAuthCode(code: String) {
        viewModelScope.launch {
            verifyAuthCodeUseCase(
                code = code,
                object : VerifyAuthCodeUseCase.Callback {
                    override fun onVerificationCompleted() {
                        setState {
                            copy(
                                authCodeStatus = VerificationState.AuthCodeStatus.VERIFIED,
                                isVerified = true,
                                remainingTimeInSec = 0
                            )
                        }
                    }

                    override fun onVerificationFailed(e: Throwable) {
                        setState {
                            copy(
                                authCodeStatus = VerificationState.AuthCodeStatus.INVALID,
                            )
                        }
                    }
                }
            )
        }
    }

    companion object :
        MavericksViewModelFactory<VerificationViewModel, VerificationState> by hiltMavericksViewModelFactory()
}
