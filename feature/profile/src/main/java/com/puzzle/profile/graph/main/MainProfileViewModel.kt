package com.puzzle.profile.graph.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.ConfigureRepository
import com.puzzle.domain.usecase.profile.GetMyProfileBasicUseCase
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.profile.graph.main.contract.MainProfileIntent
import com.puzzle.profile.graph.main.contract.MainProfileState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainProfileViewModel @AssistedInject constructor(
    @Assisted initialState: MainProfileState,
    private val configureRepository: ConfigureRepository,
    private val getMyProfileBasicUseCase: GetMyProfileBasicUseCase,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<MainProfileState>(initialState) {
    private val intents = Channel<MainProfileIntent>(BUFFERED)

    init {
        initMainProfile()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    private fun initMainProfile() = viewModelScope.launch {
        launch {
            configureRepository.isNotificationEnabled()
                .onSuccess { setState { copy(isNotificationEnabled = it) } }
                .onFailure { errorHelper.sendError(it) }
        }

        getMyProfileBasicUseCase().onSuccess {
            setState {
                copy(
                    selfDescription = it.description,
                    nickName = it.nickname,
                    age = it.age,
                    birthYear = it.birthdate.substring(2, 4),
                    height = it.height,
                    weight = it.weight,
                    location = it.location,
                    job = it.job,
                    smokingStatus = it.smokingStatus,
                )
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun onIntent(intent: MainProfileIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: MainProfileIntent) {
        when (intent) {
            is MainProfileIntent.Navigate -> navigationHelper.navigate(intent.navigationEvent)
            MainProfileIntent.OnValueTalkClick -> moveToValueTalkScreen()
            MainProfileIntent.OnBasicProfileClick -> moveToBasicProfileScreen()
            MainProfileIntent.OnValuePickClick -> moveToValuePickScreen()
        }
    }

    private fun moveToValuePickScreen() {
        navigationHelper.navigate(NavigationEvent.To(ProfileGraphDest.ValuePickProfileRoute))
    }

    private fun moveToBasicProfileScreen() {
        navigationHelper.navigate(NavigationEvent.To(ProfileGraphDest.BasicProfileRoute))
    }

    private fun moveToValueTalkScreen() {
        navigationHelper.navigate(NavigationEvent.To(ProfileGraphDest.ValueTalkProfileRoute))
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MainProfileViewModel, MainProfileState> {
        override fun create(state: MainProfileState): MainProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<MainProfileViewModel, MainProfileState> by hiltMavericksViewModelFactory()

}
