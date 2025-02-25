package com.puzzle.profile.graph.basic.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.common.toBirthDate
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

    val isInputFieldIncomplete: Boolean = contacts.isEmpty() ||
            descriptionInputState == InputState.WARNIING ||
            birthdateInputState == InputState.WARNIING ||
            locationInputState == InputState.WARNIING ||
            heightInputState == InputState.WARNIING ||
            weightInputState == InputState.WARNIING ||
            jobInputState == InputState.WARNIING ||
            imageUrlInputState == InputState.WARNIING ||
            (nickNameGuideMessage != NickNameGuideMessage.AVAILABLE
                    && nickNameGuideMessage != NickNameGuideMessage.DEFAULT)

    val nickNameStateInSavingProfile: NickNameGuideMessage =
        when {
            nickNameGuideMessage == NickNameGuideMessage.AVAILABLE -> NickNameGuideMessage.DEFAULT
            nickNameGuideMessage == NickNameGuideMessage.DEFAULT -> nickNameGuideMessage
            nickname.isBlank() -> NickNameGuideMessage.REQUIRED_FIELD
            nickNameGuideMessage == NickNameGuideMessage.LENGTH_GUIDE -> NickNameGuideMessage.DUPLICATE_CHECK_REQUIRED
            else -> nickNameGuideMessage
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
        const val MIN_HEIGHT_CM = 100
        const val MAX_HEIGHT_CM = 250
        const val MIN_WEIGHT_KG = 20
        const val MAX_WEIGHT_KG = 200

        fun getInputState(fieldValue: String?): InputState =
            if (fieldValue.isNullOrBlank()) WARNIING
            else DEFAULT

        fun getInputState(fieldValue: Boolean?): InputState =
            if (fieldValue == null) WARNIING
            else DEFAULT

        fun getInputState(fieldValue: List<Contact>): InputState =
            if (fieldValue.isEmpty() || fieldValue.find { it.content.isBlank() } != null) WARNIING
            else DEFAULT

        fun getBirthDateInputState(fieldValue: String?): InputState =
            when {
                fieldValue.isNullOrBlank() -> WARNIING
                runCatching { fieldValue.toBirthDate() }.isFailure -> WARNIING
                else -> DEFAULT
            }

        fun getHeightInputState(fieldValue: String?): InputState =
            when {
                fieldValue.isNullOrBlank() -> WARNIING
                fieldValue.toDouble().toInt() !in MIN_HEIGHT_CM..MAX_HEIGHT_CM -> WARNIING
                else -> DEFAULT
            }

        fun getWeightInputState(fieldValue: String?): InputState =
            when {
                fieldValue.isNullOrBlank() -> WARNIING
                fieldValue.toDouble().toInt() !in MIN_WEIGHT_KG..MAX_WEIGHT_KG -> WARNIING
                else -> DEFAULT
            }
    }
}
