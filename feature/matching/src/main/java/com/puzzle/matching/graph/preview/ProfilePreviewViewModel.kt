package com.puzzle.matching.graph.preview

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.matching.graph.preview.contract.ProfilePreviewIntent
import com.puzzle.matching.graph.preview.contract.ProfilePreviewSideEffect
import com.puzzle.matching.graph.preview.contract.ProfilePreviewState
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

class ProfilePreviewViewModel @AssistedInject constructor(
    @Assisted initialState: ProfilePreviewState,
    private val navigationHelper: NavigationHelper,
    internal val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<ProfilePreviewState>(initialState) {
    private val intents = Channel<ProfilePreviewIntent>(BUFFERED)
    private val _sideEffects = Channel<ProfilePreviewSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: ProfilePreviewIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: ProfilePreviewIntent) {
        when (intent) {
            ProfilePreviewIntent.OnCloseClick -> moveToBackScreen()
        }
    }

    private suspend fun moveToBackScreen() {
        _sideEffects.send(ProfilePreviewSideEffect.Navigate(NavigationEvent.NavigateUp))
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ProfilePreviewViewModel, ProfilePreviewState> {
        override fun create(state: ProfilePreviewState): ProfilePreviewViewModel
    }

    companion object :
        MavericksViewModelFactory<ProfilePreviewViewModel, ProfilePreviewState> by hiltMavericksViewModelFactory()
}
