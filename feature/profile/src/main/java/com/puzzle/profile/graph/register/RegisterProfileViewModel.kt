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
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getInputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
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

    private fun processIntent(intent: RegisterProfileIntent) {
        when (intent) {
            is RegisterProfileIntent.OnNickNameChange -> updateNickName(intent.nickName)
            is RegisterProfileIntent.OnPhotoeClick -> updateProfileImage(intent.imageUri)
            is RegisterProfileIntent.OnEditPhotoClick -> updateProfileImage(intent.imageUri)
            is RegisterProfileIntent.OnSelfDescribtionChange -> updateDescription(intent.description)
            is RegisterProfileIntent.OnBirthdayChange -> updateBirthdate(intent.birthday)
            is RegisterProfileIntent.OnHeightChange -> updateHeight(intent.height)
            is RegisterProfileIntent.OnWeightChange -> updateWeight(intent.weight)
            is RegisterProfileIntent.OnJobClick -> updateJob(intent.job)
            is RegisterProfileIntent.OnRegionClick -> updateLocation(intent.region)
            is RegisterProfileIntent.OnIsSmokeClick -> updateIsSmoke(intent.isSmoke)
            is RegisterProfileIntent.OnSnsActivityClick -> updateIsSnsActive(intent.isSnsActivity)
            is RegisterProfileIntent.OnAddContactClick -> addContact(intent.snsPlatform)
            is RegisterProfileIntent.OnDeleteContactClick -> deleteContact(intent.idx)
            is RegisterProfileIntent.OnContactSelect -> updateContact(intent.idx, intent.contact)
            is RegisterProfileIntent.ShowBottomSheet -> showBottomSheet(intent.content)
            is RegisterProfileIntent.OnSaveClick -> saveProfile(intent.registerProfileState)
            RegisterProfileIntent.HideBottomSheet -> hideBottomSheet()
            RegisterProfileIntent.OnBackClick -> moveToPrevious()
            RegisterProfileIntent.OnDuplicationCheckClick -> checkNickNameDuplication()
        }
    }

    private fun moveToPrevious() {
        withState { state ->
            if (state.currentPage == RegisterProfileState.Page.BASIC_PROFILE) {
                viewModelScope.launch {
                    _sideEffects.send(Navigate(NavigationEvent.TopLevelNavigateTo(AuthGraph)))
                }
            } else {
                setState {
                    copy(currentPage = RegisterProfileState.Page.getPreviousPage(state.currentPage))
                }
            }
        }
    }

    private fun updateProfileImage(imageUri: String) {
        setState {
            copy(
                profileImageUri = imageUri,
                profileImageUriInputState = InputState.DEFAULT,
            )
        }
    }

    private fun saveProfile(state: RegisterProfileState) {
        when (state.currentPage) {
            RegisterProfileState.Page.BASIC_PROFILE -> saveBasicProfile(state)
            RegisterProfileState.Page.VALUE_TALK -> saveValueTalk(state)
            RegisterProfileState.Page.VALUE_PICK -> saveValuePick(state)
            RegisterProfileState.Page.FINISH -> completeProfileRegister()
        }
    }

    private fun completeProfileRegister() {
        viewModelScope.launch {
            _sideEffects.send(Navigate(NavigationEvent.TopLevelNavigateTo(MatchingGraph)))
        }
    }

    private fun saveValuePick(state: RegisterProfileState) {
        setState {
            copy(valuePicks = state.valuePicks)
        }

        if (!state.isValuePickComplete) {
            eventHelper.sendEvent(PieceEvent.ShowSnackBar("모든 항목을 작성해 주세요"))
            return
        }

        setState {
            copy(currentPage = RegisterProfileState.Page.getNextPage(state.currentPage))
        }
    }

    private fun saveValueTalk(state: RegisterProfileState) {
        setState {
            copy(valueTalks = state.valueTalks)
        }

        if (!state.isValueTalkComplete) {
            eventHelper.sendEvent(PieceEvent.ShowSnackBar("모든 항목을 작성해 주세요"))
            return
        }

        setState {
            copy(currentPage = RegisterProfileState.Page.getNextPage(state.currentPage))
        }
    }

    private fun saveBasicProfile(state: RegisterProfileState) {
        // 프로필이 미완성일 때
        if (!state.isBasicProfileComplete) {
            setState {
                copy(
                    profileImageUriInputState = getInputState(state.profileImageUri),
                    nickNameGuideMessage = updatedNickNameGuideMessage,
                    descriptionInputState = getInputState(state.description),
                    birthdateInputState = getInputState(state.birthdate),
                    locationInputState = getInputState(state.location),
                    heightInputState = getInputState(state.height),
                    weightInputState = getInputState(state.weight),
                    jobInputState = getInputState(state.job),
                    isSmokeInputState = getInputState(state.isSmoke),
                    isSnsActiveInputState = getInputState(state.isSnsActive),
                    contactsInputState = getInputState(state.contacts),
                )
            }
            eventHelper.sendEvent(PieceEvent.ShowSnackBar("모든 항목을 작성해 주세요"))
            return
        }
        // 닉네임이 중복 검사를 통과한 상태, 저장 API 호출 진행
        // TODO: 실제 API 호출 후 결과에 따라 isSuccess 값을 갱신하세요.
        setState {
            copy(
                nickNameGuideMessage = NickNameGuideMessage.LENGTH_GUIDE,
                currentPage = RegisterProfileState.Page.getNextPage(state.currentPage),
            )
        }
    }

    private fun checkNickNameDuplication() {
        setState {
            // TODO: 실제 API 응답 처리
            val isSuccess = true
            copy(
                isCheckingButtonEnabled = !isSuccess,
                nickNameGuideMessage = if (isSuccess) {
                    NickNameGuideMessage.AVAILABLE
                } else {
                    NickNameGuideMessage.ALREADY_IN_USE
                },
            )
        }
    }

    private fun updateNickName(nickName: String) {
        setState {
            val newState = copy(
                nickName = nickName,
                nickNameGuideMessage = if (nickName.length > 6) {
                    NickNameGuideMessage.LENGTH_EXCEEDED_ERROR
                } else {
                    NickNameGuideMessage.LENGTH_GUIDE
                },
            )

            val isCheckingButtonEnabled = (nickName.length in 1..6)

            newState.copy(isCheckingButtonEnabled = isCheckingButtonEnabled)
        }
    }

    private fun updateDescription(description: String) {
        setState {
            copy(
                description = description,
                descriptionInputState = InputState.DEFAULT,
            )
        }
    }

    private fun updateBirthdate(birthdate: String) {
        setState {
            copy(
                birthdate = birthdate,
                birthdateInputState = InputState.DEFAULT,
            )
        }
    }

    private fun updateHeight(height: String) {
        setState {
            copy(
                height = height,
                heightInputState = InputState.DEFAULT,
            )
        }
    }

    private fun updateWeight(weight: String) {
        setState {
            copy(
                weight = weight,
                weightInputState = InputState.DEFAULT,
            )
        }
    }

    private fun updateJob(job: String) {
        setState {
            copy(
                job = job,
                jobInputState = InputState.DEFAULT,
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateLocation(location: String) {
        setState {
            copy(
                location = location,
                locationInputState = InputState.DEFAULT,
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateIsSmoke(isSmoke: Boolean) {
        setState {
            copy(
                isSmoke = isSmoke,
                isSmokeInputState = InputState.DEFAULT,
            )
        }
    }

    private fun updateIsSnsActive(isSnsActive: Boolean) {
        setState {
            copy(
                isSnsActive = isSnsActive,
                isSnsActiveInputState = InputState.DEFAULT,
            )
        }
    }

    private fun addContact(snsPlatform: SnsPlatform) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                add(Contact(snsPlatform = snsPlatform, content = ""))
            }

            copy(
                contacts = newContacts,
                contactsInputState = InputState.DEFAULT,
            )
        }


        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun deleteContact(idx: Int) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                removeAt(idx)
            }

            copy(
                contacts = newContacts,
                contactsInputState = InputState.DEFAULT,
            )
        }
    }

    private fun updateContact(idx: Int, contact: Contact) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                set(idx, contact)
            }

            copy(
                contacts = newContacts,
                contactsInputState = InputState.DEFAULT,
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
    interface Factory : AssistedViewModelFactory<RegisterProfileViewModel, RegisterProfileState> {
        override fun create(state: RegisterProfileState): RegisterProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<RegisterProfileViewModel, RegisterProfileState> by hiltMavericksViewModelFactory()
}
