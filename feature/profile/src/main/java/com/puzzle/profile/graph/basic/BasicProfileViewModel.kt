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
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getInputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
import com.puzzle.profile.graph.basic.contract.ScreenState
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
            BasicProfileIntent.CheckNickNameDuplication -> checkNickNameDuplication()
            BasicProfileIntent.SaveBasicProfile -> saveBasicProfile()
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

    private fun saveBasicProfile() {
        withState { state ->
            // 프로필이 미완성일 때
            if (state.isProfileIncomplete) {
                setState {
                    copy(
                        profileScreenState = ScreenState.SAVE_FAILED,
                        nickNameGuideMessage = nickNameStateInSavingProfile,
                        descriptionInputState = getInputState(state.description),
                        birthdateInputState = getInputState(state.birthdate),
                        locationInputState = getInputState(state.location),
                        heightInputState = getInputState(state.height),
                        weightInputState = getInputState(state.weight),
                        jobInputState = getInputState(state.job)
                    )
                }
                return@withState
            }
            // 닉네임이 중복 검사를 통과한 상태, 저장 API 호출 진행
            // TODO: 실제 API 호출 후 결과에 따라 isSuccess 값을 갱신하세요.
            val isSuccess = true
            val updatedScreenState = if (isSuccess) {
                ScreenState.SAVED
            } else {
                ScreenState.SAVE_FAILED
            }

            setState {
                copy(
                    profileScreenState = updatedScreenState,
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

            val isProfileEdited = newState != initialState
            val isCheckingButtonEnabled = isProfileEdited && (nickName.length in 1..6)

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                isCheckingButtonEnabled = isCheckingButtonEnabled
            )
        }
    }

    private fun updateDescription(description: String) {
        setState {
            val newState = copy(
                description = description,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                descriptionInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateBirthdate(birthdate: String) {
        setState {
            val newState = copy(
                birthdate = birthdate,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                birthdateInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateHeight(height: String) {
        setState {
            val newState = copy(
                height = height,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                heightInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateWeight(weight: String) {
        setState {
            val newState = copy(
                weight = weight,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                weightInputState = InputState.DEFAULT
            )
        }
    }

    private fun updateJob(job: String) {
        setState {
            val newState = copy(
                job = job,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                jobInputState = InputState.DEFAULT
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateLocation(location: String) {
        setState {
            val newState = copy(
                location = location,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
                locationInputState = InputState.DEFAULT
            )
        }
        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun updateIsSmoke(isSmoke: Boolean) {
        setState {
            val newState = copy(
                isSmoke = isSmoke,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
            )
        }
    }

    private fun updateIsSnsActive(isSnsActive: Boolean) {
        setState {
            val newState = copy(
                isSnsActive = isSnsActive,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
            )
        }
    }

    private fun addContact(snsPlatform: SnsPlatform) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                add(Contact(snsPlatform = snsPlatform, content = ""))
            }
            val newState = copy(
                contacts = newContacts,
                profileScreenState = ScreenState.SAVED,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
            )
        }


        eventHelper.sendEvent(PieceEvent.HideBottomSheet)
    }

    private fun deleteContact(idx: Int) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                removeAt(idx)
            }

            val newState = copy(
                contacts = newContacts,
                profileScreenState = ScreenState.SAVED,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
            )
        }
    }

    private fun updateContact(idx: Int, contact: Contact) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                set(idx, contact)
            }

            val newState = copy(
                contacts = newContacts,
                profileScreenState = ScreenState.SAVED,
            )

            val isProfileEdited = newState != initialState

            newState.copy(
                profileScreenState = if (isProfileEdited) {
                    ScreenState.EDITING
                } else {
                    profileScreenState
                },
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