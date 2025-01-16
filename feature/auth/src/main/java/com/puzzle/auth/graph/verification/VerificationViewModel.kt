package com.puzzle.auth.graph.verification

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.verification.contract.VerificationIntent
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect.Navigate
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.model.error.ErrorHelper
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
    private val authRepository: AuthRepository,
    private val timer: Timer,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<VerificationState>(initialState) {
    private val intents = Channel<VerificationIntent>(BUFFERED)
    private val _sideEffects = Channel<VerificationSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: VerificationIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: VerificationIntent) {
        when (intent) {
            is VerificationIntent.OnRequestAuthCodeClick -> _sideEffects.send(
                VerificationSideEffect.RequestAuthCode(intent.phoneNumber)
            )

            is VerificationIntent.OnVerifyClick -> _sideEffects.send(
                VerificationSideEffect.VerifyAuthCode(
                    phoneNumber = intent.phoneNumber,
                    code = intent.code,
                )
            )

            is VerificationIntent.Navigate -> _sideEffects.send(Navigate(intent.navigationEvent))
        }
    }

    internal fun requestAuthCode(phoneNumber: String) {
        viewModelScope.launch {
            authRepository.requestAuthCode(phoneNumber)
                .onSuccess {
                    setState {
                        copy(
                            isValidPhoneNumber = true,
                            isAuthCodeRequested = true,
                            authCodeStatus = VerificationState.AuthCodeStatus.INIT,
                        )
                    }

                    startTimer()
                }.onFailure {
                    // 정말 휴대폰 번호가 유효하지 않았을 경우
                    setState { copy(isValidPhoneNumber = false) }

                    // Todo 네트워크 통신 오류
                    errorHelper.sendError(it)
                }
        }
    }

    internal fun verifyAuthCode(phoneNumber: String, code: String) {
        viewModelScope.launch {
            authRepository.verifyAuthCode(
                phoneNumber = phoneNumber,
                code = code,
            ).onSuccess {
                // 인증에 성공했을 경우,
                timerJob?.cancel()

                setState {
                    copy(authCodeStatus = VerificationState.AuthCodeStatus.VERIFIED)
                }

                navigationHelper.navigate(
                    NavigationEvent.NavigateTo(
                        route = AuthGraphDest.SignUpRoute,
                        popUpTo = AuthGraph,
                    )
                )

                // 인증에 실패했을 경우,
                //setState { copy(authCodeStatus = VerificationState.AuthCodeStatus.INVALID) }
            }.onFailure { errorHelper.sendError(it) }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            timer.startTimer()
                .collect { remaining ->
                    setState { copy(remainingTimeInSec = remaining) }

                    if (remaining == 0) {
                        setState {
                            copy(authCodeStatus = VerificationState.AuthCodeStatus.TIME_EXPIRED)
                        }

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
    interface Factory : AssistedViewModelFactory<VerificationViewModel, VerificationState> {
        override fun create(state: VerificationState): VerificationViewModel
    }

    companion object :
        MavericksViewModelFactory<VerificationViewModel, VerificationState> by hiltMavericksViewModelFactory()
}
