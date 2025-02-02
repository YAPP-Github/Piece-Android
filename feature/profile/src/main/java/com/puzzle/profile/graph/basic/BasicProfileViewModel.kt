package com.puzzle.profile.graph.basic

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.basic.contract.BasicProfileIntent
import com.puzzle.profile.graph.basic.contract.BasicProfileSideEffect
import com.puzzle.profile.graph.basic.contract.BasicProfileState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BasicProfileViewModel @AssistedInject constructor(
    @Assisted initialState: BasicProfileState,
    internal val navigationHelper: NavigationHelper,
    private val eventHelper: EventHelper,
) : MavericksViewModel<BasicProfileState>(initialState) {

    private val intents = Channel<BasicProfileIntent>(BUFFERED)
    private val _sideEffects = Channel<BasicProfileSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()
    private val initialState: BasicProfileState = BasicProfileState()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        // TODO : initialState 초기화
    }

    internal fun onIntent(intent: BasicProfileIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private suspend fun processIntent(intent: BasicProfileIntent) {
        when (intent) {
            BasicProfileIntent.OnBackClick -> moveToBackScreen()
            is BasicProfileIntent.UpdateNickName -> updateNickName(intent.nickName)
            is BasicProfileIntent.UpdateDescribeMySelf -> updateDescription(intent.description)
            is BasicProfileIntent.UpdateBirthday -> updateBirthdate(intent.birthday)
            is BasicProfileIntent.UpdateHeight -> updateHeight(intent.height)
            is BasicProfileIntent.UpdateWeight -> updateWeight(intent.weight)
            is BasicProfileIntent.UpdateJob -> updateJob(intent.job)
            is BasicProfileIntent.UpdateRegion -> updateLocation(intent.region)
            is BasicProfileIntent.UpdateSmokeStatus -> updateIsSmoke(intent.isSmoke)
            is BasicProfileIntent.UpdateSnsActivity -> updateIsSnsActive(intent.isSnsActivity)
            is BasicProfileIntent.AddContact -> addContact(intent.snsPlatform)
            is BasicProfileIntent.DeleteContact -> deleteContact(intent.idx)
            is BasicProfileIntent.UpdateContact -> updateContact(intent.idx, intent.contact)
            is BasicProfileIntent.ShowBottomSheet -> showBottomSheet(intent.content)
            BasicProfileIntent.HideBottomSheet -> hideBottomSheet()
        }
    }

    private suspend fun moveToBackScreen() {
        _sideEffects.send(
            BasicProfileSideEffect.Navigate(NavigationEvent.NavigateUp)
        )
    }

    private fun updateNickName(nickName: String) {
        val isEdited = nickName != initialState.nickName

        setState {
            copy(
                nickName = nickName,
                isEdited = isEdited,
            )
        }
    }

    private fun updateDescription(description: String) {
        val isEdited = description != initialState.description

        setState {
            copy(
                description = description,
                isEdited = isEdited,
            )
        }
    }

    private fun updateBirthdate(birthdate: String) {
        val isEdited = birthdate != initialState.birthdate

        setState {
            copy(
                birthdate = birthdate,
                isEdited = isEdited,
            )
        }
    }

    private fun updateHeight(height: String) {
        val isEdited = height != initialState.height

        setState {
            copy(
                height = height,
                isEdited = isEdited,
            )
        }
    }

    private fun updateWeight(weight: String) {
        val isEdited = weight != initialState.weight

        setState {
            copy(
                weight = weight,
                isEdited = isEdited,
            )
        }
    }

    private fun updateJob(job: String) {
        val isEdited = job != initialState.job

        setState {
            copy(
                job = job,
                isEdited = isEdited,
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateLocation(location: String) {
        val isEdited = location != initialState.location

        setState {
            copy(
                location = location,
                isEdited = isEdited,
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateIsSmoke(isSmoke: Boolean) {
        val isEdited = isSmoke != initialState.isSmoke

        setState {
            copy(
                isSmoke = isSmoke,
                isEdited = isEdited,
            )
        }
        setState { copy(isSmoke = isSmoke) }
    }

    private fun updateIsSnsActive(isSnsActive: Boolean) {
        val isEdited = isSnsActive != initialState.isSnsActive

        setState {
            copy(
                isSnsActive = isSnsActive,
                isEdited = isEdited,
            )
        }
    }

    private fun addContact(snsPlatform: SnsPlatform) {
        val newContacts = initialState.contacts.toMutableList()

        newContacts.add(Contact(snsPlatform = snsPlatform, content = ""))

        val isEdited = newContacts != initialState.contacts

        setState {
            copy(
                contacts = newContacts,
                isEdited = isEdited,
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun deleteContact(idx: Int) {
        val newContacts = initialState.contacts.toMutableList()

        newContacts.removeAt(idx)

        val isEdited = newContacts != initialState.contacts

        setState {
            copy(
                contacts = newContacts,
                isEdited = isEdited,
            )
        }
    }

    private fun updateContact(idx: Int, contact: Contact) {
        val newContacts = initialState.contacts.toMutableList()

        newContacts[idx] = contact

        val isEdited = newContacts != initialState.contacts

        setState {
            copy(
                contacts = newContacts,
                isEdited = isEdited,
            )
        }
    }

    private fun showBottomSheet(content: @Composable () -> Unit) {
        eventHelper.sendEvent(PieceEvent.ShowBottomSheet(content))
    }

    private fun hideBottomSheet() {
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<BasicProfileViewModel, BasicProfileState> {
        override fun create(state: BasicProfileState): BasicProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<BasicProfileViewModel, BasicProfileState> by hiltMavericksViewModelFactory()

}