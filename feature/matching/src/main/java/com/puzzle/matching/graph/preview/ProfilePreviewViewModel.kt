package com.puzzle.matching.graph.preview

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.usecase.profile.GetMyValuePicksUseCase
import com.puzzle.domain.usecase.profile.GetMyValueTalksUseCase
import com.puzzle.matching.graph.preview.contract.ProfilePreviewIntent
import com.puzzle.matching.graph.preview.contract.ProfilePreviewSideEffect
import com.puzzle.matching.graph.preview.contract.ProfilePreviewState
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

class ProfilePreviewViewModel @AssistedInject constructor(
    @Assisted initialState: ProfilePreviewState,
    private val getMyValueTalksUseCase: GetMyValueTalksUseCase,
    private val getMyValuePicksUseCase: GetMyValuePicksUseCase,
    private val profileRepository: ProfileRepository,
    private val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<ProfilePreviewState>(initialState) {
    private val intents = Channel<ProfilePreviewIntent>(BUFFERED)
    private val _sideEffects = Channel<ProfilePreviewSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        initProfilePreview()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    private fun initProfilePreview() = viewModelScope.launch {
        val profileBasicJob = launch {
            profileRepository.retrieveMyProfileBasic()
                .onSuccess {
                    setState {
                        copy(myProfileBasic = it.copy(birthdate = it.birthdate.substring(2, 4)))
                    }
                }
                .onFailure { errorHelper.sendError(it) }
        }
        val valueTalksJob = launch {
            getMyValueTalksUseCase().onSuccess {
                setState { copy(myValueTalks = it) }
            }.onFailure { errorHelper.sendError(it) }
        }
        val valuePicksJob = launch {
            getMyValuePicksUseCase().onSuccess {
                setState { copy(myValuePicks = it) }
            }.onFailure { errorHelper.sendError(it) }
        }

        profileBasicJob.join()
        valueTalksJob.join()
        valuePicksJob.join()
    }

    internal fun onIntent(intent: ProfilePreviewIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: ProfilePreviewIntent) {
        when (intent) {
            ProfilePreviewIntent.OnCloseClick -> moveToBackScreen()
        }
    }

    private fun moveToBackScreen() = navigationHelper.navigate(
        NavigationEvent.To(route = MatchingGraphDest.MatchingRoute, popUpTo = true)
    )

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ProfilePreviewViewModel, ProfilePreviewState> {
        override fun create(state: ProfilePreviewState): ProfilePreviewViewModel
    }

    companion object :
        MavericksViewModelFactory<ProfilePreviewViewModel, ProfilePreviewState> by hiltMavericksViewModelFactory()
}
