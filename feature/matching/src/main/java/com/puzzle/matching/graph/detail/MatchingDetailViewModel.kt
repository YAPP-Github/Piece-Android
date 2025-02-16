package com.puzzle.matching.graph.detail

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.domain.usecase.matching.GetOpponentProfileUseCase
import com.puzzle.matching.graph.detail.contract.MatchingDetailIntent
import com.puzzle.matching.graph.detail.contract.MatchingDetailSideEffect
import com.puzzle.matching.graph.detail.contract.MatchingDetailState
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

class MatchingDetailViewModel @AssistedInject constructor(
    @Assisted initialState: MatchingDetailState,
    private val getOpponentProfileUseCase: GetOpponentProfileUseCase,
    private val matchingRepository: MatchingRepository,
    internal val navigationHelper: NavigationHelper,
    private val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<MatchingDetailState>(initialState) {
    private val intents = Channel<MatchingDetailIntent>(BUFFERED)
    private val _sideEffects = Channel<MatchingDetailSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private val matchUserId = 0 // Todo 임시

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        initMatchDetailInfo()
    }

    private fun initMatchDetailInfo() = viewModelScope.launch {
        setState { copy(isLoading = true) }

        getOpponentProfileUseCase().onSuccess { response ->
            setState { copy(profile = response) }
        }.onFailure {
            errorHelper.sendError(it)
        }.also {
            setState { copy(isLoading = false) }
        }
    }

    internal fun onIntent(intent: MatchingDetailIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: MatchingDetailIntent) {
        when (intent) {
            is MatchingDetailIntent.OnMoreClick -> showBottomSheet(intent.content)
            MatchingDetailIntent.OnMatchingDetailCloseClick ->
                navigationHelper.navigate(NavigationEvent.NavigateUp)

            MatchingDetailIntent.OnPreviousPageClick -> setPreviousPage()
            MatchingDetailIntent.OnNextPageClick -> setNextPage()
            MatchingDetailIntent.OnBlockClick -> onBlockClick()
            MatchingDetailIntent.OnReportClick -> onReportClick()
            MatchingDetailIntent.OnAcceptClick -> acceptMatching()
            MatchingDetailIntent.OnDeclineClick -> declineMatching()
        }
    }

    private fun declineMatching() = viewModelScope.launch {
        matchingRepository.refuseMatch()
            .onSuccess {
                navigationHelper.navigate(
                    NavigationEvent.NavigateTo(
                        route = MatchingGraphDest.MatchingRoute,
                        popUpTo = true,
                    )
                )
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun setNextPage() {
        setState {
            copy(currentPage = MatchingDetailState.MatchingDetailPage.getNextPage(currentPage))
        }
    }

    private fun setPreviousPage() {
        setState {
            copy(currentPage = MatchingDetailState.MatchingDetailPage.getPreviousPage(currentPage))
        }
    }

    private fun onBlockClick() {
        withState {
            navigationHelper.navigate(
                NavigationEvent.NavigateTo(
                    MatchingGraphDest.BlockRoute(
                        userId = matchUserId,
                        userName = it.profile!!.nickname,
                    )
                )
            )

            hideBottomSheet()
        }
    }

    private fun onReportClick() {
        withState {
            navigationHelper.navigate(
                NavigationEvent.NavigateTo(
                    MatchingGraphDest.ReportRoute(
                        userId = matchUserId,
                        userName = it.profile!!.nickname,
                    )
                )
            )

            hideBottomSheet()
        }
    }

    private fun acceptMatching() = viewModelScope.launch {
        matchingRepository.acceptMatching()
            .onSuccess {
                navigationHelper.navigate(
                    NavigationEvent.NavigateTo(
                        route = MatchingGraphDest.MatchingRoute,
                        popUpTo = true,
                    )
                )
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun showBottomSheet(content: @Composable () -> Unit) {
        eventHelper.sendEvent(PieceEvent.ShowBottomSheet(content))
    }

    private fun hideBottomSheet() {
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MatchingDetailViewModel, MatchingDetailState> {
        override fun create(state: MatchingDetailState): MatchingDetailViewModel
    }

    companion object :
        MavericksViewModelFactory<MatchingDetailViewModel, MatchingDetailState> by hiltMavericksViewModelFactory()
}

