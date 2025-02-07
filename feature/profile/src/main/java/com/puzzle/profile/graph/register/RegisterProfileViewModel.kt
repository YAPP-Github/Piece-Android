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

    private suspend fun processIntent(intent: RegisterProfileIntent) {
        when (intent) {
            is RegisterProfileIntent.Navigate -> handleNavigation(intent)
            is RegisterProfileIntent.UpdateNickName -> updateNickName(intent.nickName)
            is RegisterProfileIntent.UpdateProfileImage -> updateProfileImage(intent.imageUri)
            is RegisterProfileIntent.EditPhotoClick -> updateProfileImage(intent.imageUri)
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
            RegisterProfileIntent.HideBottomSheet -> hideBottomSheet()
            RegisterProfileIntent.BackClick -> Unit
            RegisterProfileIntent.DuplicationCheckClick -> checkNickNameDuplication()
            RegisterProfileIntent.SaveClick -> saveBasicProfile()
        }
    }

    private suspend fun handleNavigation(intent: RegisterProfileIntent.Navigate) {
        _sideEffects.send(Navigate(intent.navigationEvent))
    }

    private fun updateProfileImage(imageUri: String) {
        setState {
            copy(
                profileImageUri = imageUri,
                profileImageUriInputState = InputState.DEFAULT
            )
        }
    }

    private fun saveBasicProfile() {
        withState { state ->
            // 프로필이 미완성일 때
            if (state.isProfileIncomplete) {
                setState {
                    copy(
                        profileImageUriInputState = getInputState(state.profileImageUri),
                        nickNameGuideMessage = nickNameStateInSavingProfile,
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
                return@withState
            }
            // 닉네임이 중복 검사를 통과한 상태, 저장 API 호출 진행
            // TODO: 실제 API 호출 후 결과에 따라 isSuccess 값을 갱신하세요.

            setState {
                copy(
                    nickNameGuideMessage = NickNameGuideMessage.LENGTH_GUIDE,
                )
            }
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
                }
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

            newState.copy(
                isCheckingButtonEnabled = isCheckingButtonEnabled
            )
        }
    }

    private fun updateDescription(description: String) {
        setState {
            copy(
                description = description,
                descriptionInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateBirthdate(birthdate: String) {
        setState {
            copy(
                birthdate = birthdate,
                birthdateInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateHeight(height: String) {
        setState {
            copy(
                height = height,
                heightInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateWeight(weight: String) {
        setState {
            copy(
                weight = weight,
                weightInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateJob(job: String) {
        setState {
            copy(
                job = job,
                jobInputState = InputState.DEFAULT
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
                isSmokeInputState = InputState.DEFAULT
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
