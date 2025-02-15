package com.puzzle.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puzzle.common.event.EventHelper
import com.puzzle.domain.model.configure.ForceUpdate
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.domain.model.user.UserRole.NONE
import com.puzzle.domain.model.user.UserRole.PENDING
import com.puzzle.domain.model.user.UserRole.REGISTER
import com.puzzle.domain.model.user.UserRole.USER
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.repository.ConfigureRepository
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val termsRepository: TermsRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val configureRepository: ConfigureRepository,
    internal val navigationHelper: NavigationHelper,
    internal val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
) : ViewModel() {
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    init {
        handleError()
        initConfigure()
        checkRedirection()
    }

    private fun handleError() = viewModelScope.launch {
        errorHelper.errorEvent.collect { exception ->
            Log.e("Piece", exception.stackTraceToString())

            when (exception) {
                is HttpResponseException -> {
                    // Todo : HTTP 호출 에러
                    return@collect
                }

                // Todo : 그 외 IoException 등등..
            }
        }
    }

    private fun initConfigure() = viewModelScope.launch {
        val forceUpdateJob = launch { checkMinVersion() }
        val loadTermsJob = launch { loadTerms() }
        val loadValuePicksJob = launch { loadValuePicks() }
        val loadValueTalksJob = launch { loadValueTalks() }

        // 케싱시키려고 호출만 해놓습니다.
        launch { configureRepository.isNotificationEnabled() }

        forceUpdateJob.join()
        loadTermsJob.join()
        loadValuePicksJob.join()
        loadValueTalksJob.join()
    }

    private suspend fun checkMinVersion() {
        configureRepository.getForceUpdateMinVersion()
            .onSuccess { _forceUpdate.value = it }
            .onFailure { errorHelper.sendError(it) }
    }

    private suspend fun loadTerms() {
        termsRepository.loadTerms()
            .onFailure { errorHelper.sendError(it) }
    }

    private suspend fun loadValuePicks() {
        profileRepository.loadValuePickQuestions()
            .onFailure { errorHelper.sendError(it) }
    }

    private suspend fun loadValueTalks() {
        profileRepository.loadValueTalkQuestions()
            .onFailure { errorHelper.sendError(it) }
    }

    private fun checkRedirection() = viewModelScope.launch {
        // 토큰이 만료 되었을경우 종료
        authRepository.checkTokenHealth().onFailure { return@launch }

        // 토큰이 만료되지 않을경우 UserRole에 따라 화면 분기
        val userRole = async { userRepository.getUserRole() }
            .await()
            .getOrElse { return@launch }

        when (userRole) {
            REGISTER -> {
                navigationHelper.navigate(
                    NavigationEvent.NavigateTo(
                        route = AuthGraphDest.SignUpRoute,
                        popUpTo = true,
                    )
                )
            }

            PENDING, USER -> {
                navigationHelper.navigate(
                    NavigationEvent.NavigateTo(
                        route = MatchingGraphDest.MatchingRoute,
                        popUpTo = true,
                    )
                )
            }

            NONE -> navigationHelper.navigate(
                NavigationEvent.NavigateTo(
                    route = AuthGraphDest.VerificationRoute,
                    popUpTo = true,
                )
            )
        }
    }.also { _isInitialized.value = true }
}
