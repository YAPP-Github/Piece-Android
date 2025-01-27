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
            is RegisterProfileIntent.Navigate -> _sideEffects.send(Navigate(intent.navigationEvent))
            is RegisterProfileIntent.UpdateNickName -> setState { copy(nickName = intent.nickName) }
            is RegisterProfileIntent.UpdateDescribeMySelf -> setState { copy(describeMySelf = intent.description) }
            is RegisterProfileIntent.UpdateBirthday -> setState { copy(birthday = intent.birthday) }
            is RegisterProfileIntent.UpdateHeight -> setState { copy(height = intent.height) }
            is RegisterProfileIntent.UpdateWeight -> setState { copy(weight = intent.weight) }
            is RegisterProfileIntent.UpdateJob -> setState { copy(job = intent.job) }
            is RegisterProfileIntent.UpdateRegion -> setState { copy(region = intent.region) }
            is RegisterProfileIntent.UpdateSmokeStatus -> setState { copy(isSmoke = intent.isSmoke) }
            is RegisterProfileIntent.UpdateSnsActivity -> setState { copy(isSnsActivity = intent.isSnsActivity) }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<RegisterProfileViewModel, RegisterProfileState> {
        override fun create(state: RegisterProfileState): RegisterProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<RegisterProfileViewModel, RegisterProfileState> by hiltMavericksViewModelFactory()

}
