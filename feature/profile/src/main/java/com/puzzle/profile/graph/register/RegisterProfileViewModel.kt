package com.puzzle.profile.graph.register

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform
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
    private val eventHelper: EventHelper,
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
            is RegisterProfileIntent.UpdateDescribeMySelf -> updateDescription(intent.description)
            is RegisterProfileIntent.UpdateBirthday -> updateBirthdate(intent.birthday)
            is RegisterProfileIntent.UpdateHeight -> updateHeight(intent.height)
            is RegisterProfileIntent.UpdateWeight -> updateWeight(intent.weight)
            is RegisterProfileIntent.UpdateJob -> updateJob(intent.job)
            is RegisterProfileIntent.UpdateRegion -> updateLocation(intent.region)
            is RegisterProfileIntent.UpdateSmokeStatus -> updateIsSmoke(intent.isSmoke)
            is RegisterProfileIntent.UpdateSnsActivity -> updateIsSnsActive(intent.isSnsActivity)
            is RegisterProfileIntent.AddContact -> addContact(intent.snsPlatform)
            is RegisterProfileIntent.DeleteContact -> deleteContact(intent.idx)
            is RegisterProfileIntent.UpdateContact -> updateContact(intent.idx, intent.contact)
            is RegisterProfileIntent.ShowBottomSheet -> showBottomSheet(intent.content)
        }
    }

    private suspend fun handleNavigation(intent: RegisterProfileIntent.Navigate) {
        _sideEffects.send(Navigate(intent.navigationEvent))
    }

    private fun updateNickName(nickName: String) {
        setState { copy(nickName = nickName) }
    }

    private fun updateDescription(description: String) {
        setState { copy(description = description) }
    }

    private fun updateBirthdate(birthday: String) {
        setState { copy(birthdate = birthday) }
    }

    private fun updateHeight(height: String) {
        setState { copy(height = height) }
    }

    private fun updateWeight(weight: String) {
        setState { copy(weight = weight) }
    }

    private fun updateJob(job: String) {
        setState { copy(job = job) }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateLocation(region: String) {
        setState { copy(location = region) }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateIsSmoke(isSmoke: Boolean?) {
        setState { copy(isSmoke = isSmoke) }
    }

    private fun updateIsSnsActive(isSnsActivity: Boolean?) {
        setState { copy(isSnsActive = isSnsActivity) }
    }

    private fun addContact(snsPlatform: SnsPlatform) {
        setState {
            val newContacts = contacts.toMutableList()
            newContacts.add(Contact(snsPlatform = snsPlatform, content = ""))
            copy(contacts = newContacts)
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun deleteContact(idx: Int) {
        setState {
            val newContacts = contacts.toMutableList()
            newContacts.removeAt(idx)
            copy(contacts = newContacts)
        }
    }

    private fun updateContact(idx: Int, contact: Contact) {
        setState {
            val newContacts = contacts.toMutableList()
            newContacts[idx] = contact
            copy(contacts = newContacts)
        }
    }

    private fun showBottomSheet(content: @Composable () -> Unit) {
        eventHelper.sendEvent(PieceEvent.ShowBottomSheet(content))
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<RegisterProfileViewModel, RegisterProfileState> {
        override fun create(state: RegisterProfileState): RegisterProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<RegisterProfileViewModel, RegisterProfileState> by hiltMavericksViewModelFactory()
}
