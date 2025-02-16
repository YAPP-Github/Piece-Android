package com.puzzle.profile.graph.basic.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import com.puzzle.domain.model.profile.Contact

data class BasicProfileState(
    val profileScreenState: ScreenState = ScreenState.SAVED,
    val nickname: String = "",
    val isCheckingButtonEnabled: Boolean = false,
    val nickNameGuideMessage: NickNameGuideMessage = NickNameGuideMessage.DEFAULT,
    val description: String = "",
    val descriptionInputState: InputState = InputState.DEFAULT,
    val birthdate: String = "",
    val birthdateInputState: InputState = InputState.DEFAULT,
    val imageUrl: String = "",
    val imageUrlInputState: InputState = InputState.DEFAULT,
    val location: String = "",
    val locationInputState: InputState = InputState.DEFAULT,
    val height: String = "",
    val heightInputState: InputState = InputState.DEFAULT,
    val weight: String = "",
    val weightInputState: InputState = InputState.DEFAULT,
    val job: String = "",
    val jobInputState: InputState = InputState.DEFAULT,
    val isSmoke: Boolean = false,
    val isSnsActive: Boolean = false,
    val contacts: List<Contact> = emptyList(),
) : MavericksState {
    val usingSnsPlatforms = contacts.map { it.type }
        .toSet()

    val isProfileIncomplete: Boolean = contacts.isEmpty() ||
            description.isBlank() ||
            birthdate.isBlank() ||
            location.isBlank() ||
            height.isBlank() ||
            weight.isBlank() ||
            job.isBlank() ||
            nickname.isBlank() ||
            imageUrl.isBlank() ||
            (nickNameGuideMessage != NickNameGuideMessage.AVAILABLE
                    && nickNameGuideMessage != NickNameGuideMessage.DEFAULT)

    val nickNameStateInSavingProfile: NickNameGuideMessage =
        when {
            nickname.isBlank() -> {
                NickNameGuideMessage.REQUIRED_FIELD
            }

            nickname.length in 1..6 &&
                    nickNameGuideMessage != NickNameGuideMessage.AVAILABLE -> {
                NickNameGuideMessage.DUPLICATE_CHECK_REQUIRED
            }

            nickNameGuideMessage == NickNameGuideMessage.LENGTH_GUIDE -> {
                nickNameGuideMessage
            }

            else -> {
                nickNameGuideMessage
            }
        }
}

enum class ScreenState {
    EDITING,
    SAVED,
    SAVE_FAILED,
}

enum class NickNameGuideMessage(
    val inputState: InputState,
    val guideMessageId: Int
) {
    DEFAULT(
        inputState = InputState.DEFAULT,
        guideMessageId = R.string.empty,
    ),
    LENGTH_GUIDE(
        inputState = InputState.DEFAULT,
        guideMessageId = R.string.basic_profile_nickname_length_guide
    ),
    REQUIRED_FIELD(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.basic_profile_required_field
    ),
    LENGTH_EXCEEDED_ERROR(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.basic_profile_nickname_length_exceed_error
    ),
    ALREADY_IN_USE(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.basic_profile_nickname_already_in_use
    ),
    AVAILABLE(
        inputState = InputState.DEFAULT,
        guideMessageId = R.string.basic_profile_nickname_available
    ),
    DUPLICATE_CHECK_REQUIRED(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.basic_profile_nickname_duplicate_check_required
    ),
}

enum class InputState {
    DEFAULT,
    WARNIING,
    ;

    companion object {
        fun getInputState(fieldValue: String?): InputState =
            if (fieldValue.isNullOrBlank()) WARNIING
            else DEFAULT

        fun getInputState(fieldValue: Boolean?): InputState =
            if (fieldValue == null) WARNIING
            else DEFAULT

        fun getInputState(fieldValue: List<Contact>): InputState =
            if (fieldValue.isEmpty() || fieldValue.find { it.content.isBlank() } != null) WARNIING
            else DEFAULT
    }
}
