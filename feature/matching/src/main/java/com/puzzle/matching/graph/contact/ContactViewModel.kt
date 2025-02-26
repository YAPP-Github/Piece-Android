package com.puzzle.matching.graph.contact

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.repository.MatchingRepository
import com.puzzle.domain.usecase.matching.GetOpponentProfileUseCase
import com.puzzle.matching.graph.contact.contract.ContactIntent
import com.puzzle.matching.graph.contact.contract.ContactSideEffect
import com.puzzle.matching.graph.contact.contract.ContactState
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

class ContactViewModel @AssistedInject constructor(
    @Assisted initialState: ContactState,
    private val getOpponentProfileUseCase: GetOpponentProfileUseCase,
    private val matchingRepository: MatchingRepository,
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<ContactState>(initialState) {
    private val intents = Channel<ContactIntent>(BUFFERED)
    private val _sideEffects = Channel<ContactSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        initContactInfo()
    }

    private fun initContactInfo() = viewModelScope.launch {
        setState { copy(isLoading = true) }
        val opponentProfileJob = launch {
            getOpponentProfileUseCase().onSuccess { response ->
                setState {
                    copy(
                        nickName = response.nickname,
                        imageUrl = response.imageUrl,
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
        }

        val opponentContactsDeferred = launch {
            matchingRepository.getOpponentContacts().onSuccess { response ->
                setState {
                    copy(
                        contacts = response,
                        selectedContact = response.first()
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
        }

        opponentProfileJob.join()
        opponentContactsDeferred.join()

        setState { copy(isLoading = false) }
    }

    internal fun onIntent(intent: ContactIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: ContactIntent) {
        when (intent) {
            ContactIntent.OnCloseClick -> moveToBackScreen()
            is ContactIntent.OnContactClick -> updateSelectedContact(intent.selectedContact)
        }
    }

    private fun updateSelectedContact(selectedContact: Contact) {
        setState {
            copy(selectedContact = selectedContact)
        }
    }

    private suspend fun moveToBackScreen() {
        _sideEffects.send(ContactSideEffect.Navigate(NavigationEvent.Up))
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<ContactViewModel, ContactState> {
        override fun create(state: ContactState): ContactViewModel
    }

    companion object :
        MavericksViewModelFactory<ContactViewModel, ContactState> by hiltMavericksViewModelFactory()
}
