package com.puzzle.auth.graph.login

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.login.contract.LoginIntent
import com.puzzle.auth.graph.login.contract.LoginSideEffect
import com.puzzle.auth.graph.login.contract.LoginState
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.user.UserRole.NONE
import com.puzzle.domain.model.user.UserRole.PENDING
import com.puzzle.domain.model.user.UserRole.REGISTER
import com.puzzle.domain.model.user.UserRole.USER
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.MatchingGraphDest
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

class LoginViewModel @AssistedInject constructor(
    @Assisted initialState: LoginState,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigationHelper: NavigationHelper,
    internal val errorHelper: ErrorHelper,
) : MavericksViewModel<LoginState>(initialState) {
    private val _intents = Channel<LoginIntent>(BUFFERED)

    private val _sideEffects = Channel<LoginSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: LoginIntent) = viewModelScope.launch {
        _intents.send(intent)
    }

    private suspend fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Navigate -> navigationHelper.navigate(intent.navigationEvent)
            is LoginIntent.LoginOAuth -> {
                setState { copy(isLoading = true) }
                when (intent.oAuthProvider) {
                    OAuthProvider.KAKAO -> _sideEffects.send(LoginSideEffect.LoginKakao)
                    OAuthProvider.GOOGLE -> _sideEffects.send(LoginSideEffect.LoginGoogle)
                }
            }
        }
    }

    internal fun loginOAuth(oAuthProvider: OAuthProvider, token: String) = viewModelScope.launch {
        authRepository.loginOauth(
            oAuthProvider = oAuthProvider,
            oauthCredential = token,
        ).onSuccess { userRole ->
            Log.d("test", userRole.toString())

            when (userRole) {
                REGISTER -> {
                    navigationHelper.navigate(
                        NavigationEvent.To(
                            route = AuthGraphDest.SignUpRoute,
                            popUpTo = true,
                        )
                    )
                }

                PENDING, USER -> {
                    navigationHelper.navigate(
                        NavigationEvent.To(
                            route = MatchingGraphDest.MatchingRoute,
                            popUpTo = true,
                        )
                    )
                }

                NONE -> navigationHelper.navigate(
                    NavigationEvent.To(
                        route = AuthGraphDest.VerificationRoute,
                        popUpTo = true,
                    )
                )
            }
        }.onFailure { errorHelper.sendError(it) }
            .also { setState { copy(isLoading = false) } }
    }

    internal fun loginFailure(throwable: Throwable) = viewModelScope.launch {
        setState { copy(isLoading = false) }
        errorHelper.sendError(throwable)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<LoginViewModel, LoginState> {
        override fun create(state: LoginState): LoginViewModel
    }

    companion object :
        MavericksViewModelFactory<LoginViewModel, LoginState> by hiltMavericksViewModelFactory()
}
