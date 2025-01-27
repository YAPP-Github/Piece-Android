package com.puzzle.profile.graph.register

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect.Navigate
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterProfileViewModel @AssistedInject constructor(
    @Assisted initialState: RegisterProfileState,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<RegisterProfileState>(initialState) {
    private val intents = Channel<RegisterProfileIntent>(BUFFERED)
    private val _sideEffects = Channel<RegisterProfileSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: RegisterProfileIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: RegisterProfileIntent) {
        when (intent) {
            is RegisterProfileIntent.Navigate -> handleNavigation(intent)
            is RegisterProfileIntent.UpdateNickName -> updateNickName(intent.nickName)
            is RegisterProfileIntent.UpdateDescribeMySelf -> updateDescribeMySelf(intent.description)
            is RegisterProfileIntent.UpdateBirthday -> updateBirthday(intent.birthday)
            is RegisterProfileIntent.UpdateHeight -> updateHeight(intent.height)
            is RegisterProfileIntent.UpdateWeight -> updateWeight(intent.weight)
            is RegisterProfileIntent.UpdateJob -> updateJob(intent.job)
            is RegisterProfileIntent.UpdateRegion -> updateRegion(intent.region)
            is RegisterProfileIntent.UpdateSmokeStatus -> updateSmokeStatus(intent.isSmoke)
            is RegisterProfileIntent.UpdateSnsActivity -> updateSnsActivity(intent.isSnsActivity)
            RegisterProfileIntent.OnAddContactsClicked -> handleAddContactsClicked()
            RegisterProfileIntent.OnJobDropDownClicked -> handleJobDropDownClicked()
            RegisterProfileIntent.OnRegionDropDownClicked -> handleRegionDropDownClicked()
        }
    }

    private suspend fun handleNavigation(intent: RegisterProfileIntent.Navigate) {
        _sideEffects.send(Navigate(intent.navigationEvent))
    }

    private fun updateNickName(nickName: String) {
        setState { copy(nickName = nickName) }
    }

    private fun updateDescribeMySelf(description: String) {
        setState { copy(describeMySelf = description) }
    }

    private fun updateBirthday(birthday: String) {
        setState { copy(birthday = birthday) }
    }

    private fun updateHeight(height: String) {
        setState { copy(height = height) }
    }

    private fun updateWeight(weight: String) {
        setState { copy(weight = weight) }
    }

    private fun updateJob(job: String) {
        setState { copy(job = job) }
    }

    private fun updateRegion(region: String) {
        setState { copy(region = region) }
    }

    private fun updateSmokeStatus(isSmoke: Boolean?) {
        setState { copy(isSmoke = isSmoke) }
    }

    private fun updateSnsActivity(isSnsActivity: Boolean?) {
        setState { copy(isSnsActivity = isSnsActivity) }
    }

    private fun handleAddContactsClicked() {
        // TODO: Implement add contacts logic
    }

    private fun handleJobDropDownClicked() {
        // TODO: Implement job dropdown logic
    }

    private fun handleRegionDropDownClicked() {
        // TODO: Implement region dropdown logic
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<RegisterProfileViewModel, RegisterProfileState> {
        override fun create(state: RegisterProfileState): RegisterProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<RegisterProfileViewModel, RegisterProfileState> by hiltMavericksViewModelFactory()
}
