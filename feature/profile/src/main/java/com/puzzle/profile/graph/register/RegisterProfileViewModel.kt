package com.puzzle.profile.graph.register

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.common.event.EventHelper
import com.puzzle.common.event.PieceEvent
import com.puzzle.common.toBirthDate
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.profile.ValuePickAnswer
import com.puzzle.domain.model.profile.ValueTalkAnswer
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.domain.usecase.profile.GetMyProfileBasicUseCase
import com.puzzle.domain.usecase.profile.GetMyValuePicksUseCase
import com.puzzle.domain.usecase.profile.GetMyValueTalksUseCase
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getBirthDateInputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getHeightInputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getInputState
import com.puzzle.profile.graph.basic.contract.InputState.Companion.getWeightInputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO
import com.puzzle.profile.graph.register.model.ValueTalkRegisterRO
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterProfileViewModel @AssistedInject constructor(
    @Assisted initialState: RegisterProfileState,
    private val getMyProfileBasicUseCase: GetMyProfileBasicUseCase,
    private val getMyValueTalksUseCase: GetMyValueTalksUseCase,
    private val getMyValuePicksUseCase: GetMyValuePicksUseCase,
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
    internal val navigationHelper: NavigationHelper,
    private val eventHelper: EventHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<RegisterProfileState>(initialState) {
    private val intents = Channel<RegisterProfileIntent>(BUFFERED)

    init {
        initProfileData()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    private fun initProfileData() = viewModelScope.launch {
        val valuePickJob = launch { retrieveValuePick() }
        val valueTalkJob = launch { retrieveValueTalk() }
        valuePickJob.join()
        valueTalkJob.join()

        val userRole = userRepository.getUserRole().first()
        setState { copy(userRole = userRole) }

        if (userRole == UserRole.PENDING) {
            val myValuePickJob = launch { updateValuePicks() }
            val myValueTalkJob = launch { updateValueTalks() }
            val myProfileBasicJob = launch { updateProfileBasic() }

            myValuePickJob.join()
            myValueTalkJob.join()
            myProfileBasicJob.join()
        }
    }

    private suspend fun updateValuePicks() {
        getMyValuePicksUseCase()
            .onSuccess { myValuePicks ->
                setState {
                    copy(
                        valuePicks = valuePicks.map { valuePick ->
                            myValuePicks.find { it.id == valuePick.id }?.let { myValuePick ->
                                ValuePickRegisterRO(
                                    id = myValuePick.id,
                                    category = myValuePick.category,
                                    question = myValuePick.question,
                                    answerOptions = myValuePick.answerOptions,
                                    selectedAnswer = myValuePick.selectedAnswer,
                                )
                            } ?: valuePick
                        }
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
    }

    private suspend fun updateValueTalks() {
        getMyValueTalksUseCase()
            .onSuccess { myValueTalks ->
                setState {
                    copy(
                        valueTalks = valueTalks.map { valueTalk ->
                            myValueTalks.find { it.id == valueTalk.id }?.let { myValueTalk ->
                                ValueTalkRegisterRO(
                                    id = myValueTalk.id,
                                    category = myValueTalk.category,
                                    title = myValueTalk.title,
                                    guides = myValueTalk.guides,
                                    placeholder = myValueTalk.placeholder,
                                    answer = myValueTalk.answer,
                                )
                            } ?: valueTalk
                        }
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
    }

    private suspend fun updateProfileBasic() {
        getMyProfileBasicUseCase()
            .onSuccess { myProfileBasic ->
                setState {
                    copy(
                        description = myProfileBasic.description,
                        nickname = myProfileBasic.nickname,
                        birthdate = myProfileBasic.birthdate,
                        height = myProfileBasic.height.toString(),
                        weight = myProfileBasic.weight.toString(),
                        location = myProfileBasic.location,
                        job = myProfileBasic.job,
                        isSmoke = myProfileBasic.isSmoke(),
                        isSnsActive = myProfileBasic.isSnsActive(),
                        imageUrl = myProfileBasic.imageUrl,
                        contacts = myProfileBasic.contacts,
                    )
                }
            }.onFailure { errorHelper.sendError(it) }
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
                        placeholder = it.placeholder,
                        answer = "",
                    )
                }
                setState { copy(valueTalks = result) }
            }
            .onFailure { errorHelper.sendError(it) }
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
            RegisterProfileIntent.OnHomeClick -> navigateToHome()
            RegisterProfileIntent.HideBottomSheet -> hideBottomSheet()
            RegisterProfileIntent.OnBackClick -> moveToPrevious()
            RegisterProfileIntent.OnDuplicationCheckClick -> checkNickNameDuplication()
            RegisterProfileIntent.OnCheckMyProfileClick -> navigateToProfilePreview()
        }
    }

    private fun moveToPrevious() {
        withState { state ->
            if (state.currentPage == RegisterProfileState.Page.BASIC_PROFILE) {
                viewModelScope.launch { navigationHelper.navigate(NavigationEvent.Up) }
            } else {
                state.currentPage.getPreviousPage()
                    ?.let { previousPage -> setState { copy(currentPage = previousPage) } }
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
            RegisterProfileState.Page.SUMMATION -> Unit
            RegisterProfileState.Page.FINISH -> navigateToProfilePreview()
        }
    }

    private fun navigateToProfilePreview() {
        navigationHelper.navigate(NavigationEvent.TopLevelTo(MatchingGraphDest.ProfilePreviewRoute))
    }

    private fun saveValuePick(state: RegisterProfileState) {
        setState { copy(valuePicks = state.valuePicks) }

        if (!state.isValuePickComplete) {
            eventHelper.sendEvent(PieceEvent.ShowSnackBar("모든 항목을 작성해 주세요"))
            return
        }

        state.currentPage.getNextPage()?.let { nextPage ->
            setState { copy(currentPage = nextPage) }
        }

        viewModelScope.launch {
            when (state.userRole) {
                UserRole.PENDING -> updateProfile(state)
                else -> uploadProfile(state)
            }
        }
    }

    private suspend fun updateProfile(state: RegisterProfileState) {
        profileRepository.updateProfile(
            birthdate = state.birthdate.toBirthDate(),
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
            loadMyProfile()
            setState { copy(currentPage = RegisterProfileState.Page.FINISH) }
        }.onFailure { exception ->
            if (exception is HttpResponseException) {
                exception.msg?.let { message ->
                    eventHelper.sendEvent(PieceEvent.ShowSnackBar(msg = message))
                }
                setState { copy(currentPage = RegisterProfileState.Page.VALUE_PICK) }
                return@onFailure
            }

            errorHelper.sendError(exception)
            setState { copy(currentPage = RegisterProfileState.Page.VALUE_PICK) }
        }
    }

    private suspend fun uploadProfile(state: RegisterProfileState) {
        profileRepository.uploadProfile(
            birthdate = state.birthdate.toBirthDate(),
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
            loadMyProfile()
            setState { copy(currentPage = RegisterProfileState.Page.FINISH) }
        }.onFailure { exception ->
            if (exception is HttpResponseException) {
                exception.msg?.let { message ->
                    eventHelper.sendEvent(PieceEvent.ShowSnackBar(msg = message))
                }
                setState { copy(currentPage = RegisterProfileState.Page.VALUE_PICK) }
                return@onFailure
            }

            errorHelper.sendError(exception)
            setState { copy(currentPage = RegisterProfileState.Page.VALUE_PICK) }
        }
    }

    private suspend fun loadMyProfile() = coroutineScope {
        val profileBasicJob = launch {
            profileRepository.loadMyProfileBasic()
                .onFailure { errorHelper.sendError(it) }
        }
        val valueTalksJob = launch {
            profileRepository.loadMyValuePicks()
                .onFailure { errorHelper.sendError(it) }
        }
        val valuePicksJob = launch {
            profileRepository.loadMyValueTalks()
                .onFailure { errorHelper.sendError(it) }
        }

        profileBasicJob.join()
        valueTalksJob.join()
        valuePicksJob.join()
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
        val updatedState = state.copy(
            imageUrlInputState = getInputState(state.imageUrl),
            nickNameGuideMessage = state.nickNameStateInSavingProfile,
            descriptionInputState = getInputState(state.description),
            birthdateInputState = getBirthDateInputState(state.birthdate),
            locationInputState = getInputState(state.location),
            heightInputState = getHeightInputState(
                fieldValue = state.height,
                isInSave = true
            ),
            weightInputState = getWeightInputState(
                fieldValue = state.weight,
                isInSave = true
            ),
            jobInputState = getInputState(state.job),
            isSmokeInputState = getInputState(state.isSmoke),
            isSnsActiveInputState = getInputState(state.isSnsActive),
            contactsInputState = getInputState(state.contacts)
        )

        setState { updatedState }

        if (updatedState.isInputFieldIncomplete) return

        // 닉네임이 중복 검사를 통과한 상태, 저장 API 호출 진행
        state.currentPage.getNextPage()?.let { nextPage ->
            setState { copy(currentPage = nextPage) }
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
                heightInputState = getHeightInputState(
                    fieldValue = height,
                    isInSave = false
                ),
            )
        }
    }

    private fun updateWeight(weight: String) {
        setState {
            copy(
                weight = weight,
                weightInputState = getWeightInputState(
                    fieldValue = weight,
                    isInSave = false
                ),
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

    private fun navigateToHome() {
        navigationHelper.navigate(NavigationEvent.TopLevelTo(MatchingGraphDest.MatchingRoute))
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
