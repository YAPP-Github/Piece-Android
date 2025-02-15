package com.puzzle.setting.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.toBlockSyncFormattedTime
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationEvent.NavigateTo
import com.puzzle.navigation.NavigationHelper
import com.puzzle.navigation.SettingGraphDest
import com.puzzle.setting.BuildConfig
import com.puzzle.setting.graph.main.contract.SettingIntent
import com.puzzle.setting.graph.main.contract.SettingSideEffect
import com.puzzle.setting.graph.main.contract.SettingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingViewModel @AssistedInject constructor(
    @Assisted initialState: SettingState,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    internal val navigationHelper: NavigationHelper,
    internal val errorHelper: ErrorHelper,
) : MavericksViewModel<SettingState>(initialState) {
    private val _intents = Channel<SettingIntent>(BUFFERED)

    private val _sideEffects = Channel<SettingSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        initSetting()

        _intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    private fun initSetting() = viewModelScope.launch {
        launch {
            userRepository.getUserSettingInfo()
                .onSuccess {
                    setState {
                        copy(
                            isContactBlocked = it.isAcquaintanceBlockEnabled,
                            isPushNotificationEnabled = it.isNotificationEnabled,
                            isMatchingNotificationEnabled = it.isMatchNotificationEnabled,
                        )
                    }
                }
                .onFailure { errorHelper.sendError(it) }
        }

        launch {
            userRepository.getBlockSyncTime()
                .onSuccess { setState { copy(lastRefreshTime = it.toBlockSyncFormattedTime()) } }
                .onFailure { errorHelper.sendError(it) }
        }
    }

    internal fun setAppVersion(version: String) = setState {
        copy(version = version)
    }

    internal fun onIntent(intent: SettingIntent) = viewModelScope.launch {
        _intents.send(intent)
    }

    private suspend fun processIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.OnWithdrawClick -> moveToWithdrawScreen()
            is SettingIntent.OnLogoutClick -> logout()
            SettingIntent.OnInquiryClick -> navigateToWebView(
                "문의하기",
                BuildConfig.PIECE_CHANNEL_TALK_URL
            )

            SettingIntent.OnNoticeClick -> navigateToWebView("공지사항", BuildConfig.PIECE_NOTICE_URL)
            SettingIntent.OnPrivacyAndPolicyClick -> navigateToWebView(
                "개인정보처리방침",
                BuildConfig.PIECE_PRIVACY_AND_POLICY_URL
            )

            SettingIntent.OnTermsOfUseClick -> navigateToWebView(
                "이용약관",
                BuildConfig.PIECE_TERMS_OF_USE_URL
            )

            SettingIntent.UpdateBlockAcquaintances -> updateBlockAcquaintances()
            SettingIntent.UpdateMatchNotification -> updateMatchNotification()
            SettingIntent.UpdatePushNotification -> updatePushNotification()
        }
    }

    private fun moveToWithdrawScreen() =
        navigationHelper.navigate(NavigateTo(SettingGraphDest.WithdrawRoute))

    private fun navigateToWebView(title: String, url: String) =
        navigationHelper.navigate(
            NavigateTo(
                SettingGraphDest.WebViewRoute(
                    title = title,
                    url = url
                )
            )
        )

    private suspend fun logout() {
        authRepository.logout()
            .onSuccess { navigationHelper.navigate(NavigationEvent.TopLevelNavigateTo(AuthGraph)) }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun updateBlockAcquaintances() = withState { state ->
        viewModelScope.launch {
            userRepository.updateBlockAcquaintances(!state.isContactBlocked)
                .onSuccess { setState { copy(isContactBlocked = !state.isContactBlocked) } }
                .onFailure { errorHelper.sendError(it) }
        }
    }

    private fun updateMatchNotification() = withState { state ->
        viewModelScope.launch {
            userRepository.updateMatchNotification(!state.isMatchingNotificationEnabled)
                .onSuccess { setState { copy(isMatchingNotificationEnabled = !state.isMatchingNotificationEnabled) } }
                .onFailure { errorHelper.sendError(it) }
        }
    }

    private fun updatePushNotification() = withState { state ->
        viewModelScope.launch {
            userRepository.updatePushNotification(!state.isPushNotificationEnabled)
                .onSuccess { setState { copy(isPushNotificationEnabled = !state.isPushNotificationEnabled) } }
                .onFailure { errorHelper.sendError(it) }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SettingViewModel, SettingState> {
        override fun create(state: SettingState): SettingViewModel
    }

    companion object :
        MavericksViewModelFactory<SettingViewModel, SettingState> by hiltMavericksViewModelFactory()
}
