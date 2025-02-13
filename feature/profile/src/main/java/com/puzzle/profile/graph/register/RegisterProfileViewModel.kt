package com.puzzle.profile.graph.register

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.usecase.profile.UploadProfileUseCase
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getInputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect.Navigate
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO
import com.puzzle.profile.graph.register.model.ValueTalkRegisterRO
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
    private val uploadProfileUseCase: UploadProfileUseCase,
    private val profileRepository: ProfileRepository,
    internal val navigationHelper: NavigationHelper,
    private val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<RegisterProfileState>(initialState) {
    private val intents = Channel<RegisterProfileIntent>(BUFFERED)
    private val _sideEffects = Channel<RegisterProfileSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        initProfileData()

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
            is RegisterProfileIntent.OnProfileImageChanged -> updateProfileImage(intent.imageUri)
            is RegisterProfileIntent.OnSelfDescriptionChange -> updateDescription(intent.description)
            is RegisterProfileIntent.OnBirthdateChange -> updateBirthdate(intent.birthday)
            is RegisterProfileIntent.OnHeightChange -> updateHeight(intent.height)
            is RegisterProfileIntent.OnWeightChange -> updateWeight(intent.weight)
            is RegisterProfileIntent.OnJobClick -> updateJob(intent.job)
            is RegisterProfileIntent.OnRegionClick -> updateLocation(intent.region)
            is RegisterProfileIntent.OnIsSmokeClick -> updateIsSmoke(intent.isSmoke)
            is RegisterProfileIntent.OnSnsActivityClick -> updateIsSnsActive(intent.isSnsActivity)
            is RegisterProfileIntent.OnAddContactClick -> addContact(intent.contactType)
            is RegisterProfileIntent.OnDeleteContactClick -> deleteContact(intent.idx)
            is RegisterProfileIntent.OnContactSelect -> updateContact(intent.idx, intent.contact)
            is RegisterProfileIntent.ShowBottomSheet -> showBottomSheet(intent.content)
            is RegisterProfileIntent.OnSaveClick -> saveProfile(intent.registerProfileState)
            RegisterProfileIntent.HideBottomSheet -> hideBottomSheet()
            RegisterProfileIntent.OnBackClick -> moveToPrevious()
            RegisterProfileIntent.OnDuplicationCheckClick -> checkNickNameDuplication()
            RegisterProfileIntent.OnHomeClick -> moveToMatchingMainScreen()
            RegisterProfileIntent.OnCheckMyProfileClick -> moveToProfilePreviewScreen()
        }
    }

    private fun initProfileData() = viewModelScope.launch {
        val valuePickJob = launch { retrieveValuePick() }
        val valueTalkJob = launch { retrieveValueTalk() }
        valuePickJob.join()
        valueTalkJob.join()
    }

    private fun moveToProfilePreviewScreen() {
        navigationHelper.navigate(NavigationEvent.NavigateTo(MatchingGraphDest.ProfilePreviewRoute))
    }

    private fun moveToMatchingMainScreen() {
        navigationHelper.navigate(NavigationEvent.NavigateTo(MatchingGraphDest.MatchingRoute))
    }

    private suspend fun retrieveValuePick() {
        profileRepository.retrieveValuePickQuestion()
            .onSuccess { valuePickQuestions ->
                setState {
                    copy(
                        valuePicks = valuePickQuestions.map {
                            ValuePickRegisterRO(
                                id = it.id,
                                category = it.category,
                                question = it.question,
                                answerOptions = it.answerOptions,
                                selectedAnswer = null,
                            )
                        }
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
    }

    private suspend fun retrieveValueTalk() {
        profileRepository.retrieveValueTalkQuestion()
            .onSuccess { valueTalkQuestions ->
                val result = valueTalkQuestions.map {
                    ValueTalkRegisterRO(
                        id = it.id,
                        category = it.category,
                        title = it.title,
                        guides = it.guides,
                        answer = "",
                    )
                }
                setState {
                    copy(
                        valueTalks = result
                    )
                }
            }
            .onFailure { errorHelper.sendError(it) }
    }

    private fun moveToPrevious() {
        withState { state ->
            if (state.currentPage == RegisterProfileState.Page.BASIC_PROFILE) {
                viewModelScope.launch { _sideEffects.send(Navigate(NavigationEvent.NavigateUp)) }
            } else {
                state.currentPage.getPreviousPage()
                    ?.let { previosPage ->
                        setState { copy(currentPage = previosPage) }
                    }
            }
        }
    }

    private fun updateProfileImage(imageUri: String) {
        setState {
            copy(
                imageUrl = imageUri,
                imageUrlInputState = InputState.DEFAULT,
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

        viewModelScope.launch {
            uploadProfileUseCase(
                birthdate = state.birthdate,
                description = state.description,
                height = state.height.toInt(),
                weight = state.weight.toInt(),
                imageUrl = state.imageUrl.toString(),
                job = state.job,
                location = state.location,
                nickname = state.nickname,
                smokingStatus = if (state.isSmoke!!) "흡연" else "비흡연",
                snsActivityLevel = if (state.isSnsActive!!) "활동" else "은둔",
                contacts = state.contacts,
                valuePicks = state.valuePicks.map { valuePick ->
                    ValuePickAnswer(
                        valuePickId = valuePick.id,
                        selectedAnswer = valuePick.selectedAnswer,
                    )
                },
                valueTalks = state.valueTalks.map {
                    ValueTalkAnswer(
                        valueTalkId = it.id,
                        answer = it.answer,
                    )
                },
            ).onSuccess {
                state.currentPage.getNextPage()?.let { nextPage ->
                    setState { copy(currentPage = nextPage) }
                }
            }.onFailure { errorHelper.sendError(it) }
        }
    }

    private fun saveValueTalk(state: RegisterProfileState) {
        setState { copy(valueTalks = state.valueTalks) }

        if (!state.isValueTalkComplete) {
            eventHelper.sendEvent(PieceEvent.ShowSnackBar("모든 항목을 작성해 주세요"))
            return
        }

        state.currentPage.getNextPage()?.let { nextPage ->
            setState { copy(currentPage = nextPage) }
        }
    }

    private fun saveBasicProfile(state: RegisterProfileState) {
        // 프로필이 미완성일 때
        if (!state.isBasicProfileComplete) {
            setState {
                copy(
                    imageUrlInputState = getInputState(state.imageUrl),
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
        state.currentPage.getNextPage()?.let { nextPage ->
            setState {
                copy(
                    currentPage = nextPage,
                    nickNameGuideMessage = NickNameGuideMessage.LENGTH_GUIDE,
                )
            }
        }
    }

    private fun checkNickNameDuplication() = withState { state ->
        viewModelScope.launch {
            profileRepository.checkNickname(state.nickname)
                .onSuccess { result ->
                    setState {
                        copy(
                            isCheckingButtonEnabled = !result,
                            nickNameGuideMessage = if (result) {
                                NickNameGuideMessage.AVAILABLE
                            } else {
                                NickNameGuideMessage.ALREADY_IN_USE
                            },
                        )
                    }
                }.onFailure { errorHelper.sendError(it) }
        }
    }

    private fun updateNickName(nickName: String) {
        setState {
            val newState = copy(
                nickname = nickName,
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

    private fun addContact(contactType: ContactType) {
        setState {
            val newContacts = contacts.toMutableList().apply {
                add(Contact(type = contactType, content = ""))
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
            val newContacts = contacts.toMutableList()
                .apply { set(idx, contact) }

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
    interface Factory :
        AssistedViewModelFactory<RegisterProfileViewModel, RegisterProfileState> {
        override fun create(state: RegisterProfileState): RegisterProfileViewModel
    }

    companion object :
        MavericksViewModelFactory<RegisterProfileViewModel, RegisterProfileState> by hiltMavericksViewModelFactory()
}
