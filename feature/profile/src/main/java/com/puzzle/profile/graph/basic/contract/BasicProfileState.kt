package com.puzzle.profile.graph.basic.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform

data class BasicProfileState(
    val profileScreenState: ScreenState = ScreenState.SAVED,
    val nickName: String = "수줍은 수달",
    val isCheckingButtonEnabled: Boolean = false,
    val nickNameGuideMessage: NickNameGuideMessage = NickNameGuideMessage.LENGTH_GUIDE,
    val description: String = "요리와 음악을 좋아하는",
    val descriptionInputState: InputState = InputState.DEFAULT,
    val birthdate: String = "19990909",
    val birthdateInputState: InputState = InputState.DEFAULT,
    val location: String = "서울특별시",
    val locationInputState: InputState = InputState.DEFAULT,
    val height: String = "180",
    val heightInputState: InputState = InputState.DEFAULT,
    val weight: String = "72",
    val weightInputState: InputState = InputState.DEFAULT,
    val job: String = "프리랜서",
    val jobInputState: InputState = InputState.DEFAULT,
    val isSmoke: Boolean = false,
    val isSnsActive: Boolean = false,
    val contacts: List<Contact> = listOf(
        Contact(
            snsPlatform = SnsPlatform.KAKAO_TALK_ID,
            content = "puzzle1234",
        ),
        Contact(
            snsPlatform = SnsPlatform.INSTAGRAM_ID,
            content = "puzzle1234",
        ),
        Contact(
            snsPlatform = SnsPlatform.PHONE_NUMBER,
            content = "010-0000-0000",
        ),
        Contact(
            snsPlatform = SnsPlatform.OPEN_CHAT_URL,
            content = "https://open.kakao.com/o/s5aqIX1g",
        ),
    ),
) : MavericksState {
    val usingSnsPlatforms = contacts.map { it.snsPlatform }
        .toSet()

    val isProfileIncomplete: Boolean = contacts.isEmpty() ||
            description.isBlank() ||
            birthdate.isBlank() ||
            location.isBlank() ||
            height.isBlank() ||
            weight.isBlank() ||
            job.isBlank() ||
            nickName.isBlank() ||
            nickNameGuideMessage != NickNameGuideMessage.AVAILABLE

    val nickNameStateInSavingProfile: NickNameGuideMessage =
        when {
            nickName.isBlank() -> {
                NickNameGuideMessage.REQUIRED_FIELD
            }

            nickName.length in 1..6 &&
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
    LENGTH_GUIDE(
        inputState = InputState.DEFAULT,
        guideMessageId = R.string.base_profile_nickname_length_guide
    ),
    REQUIRED_FIELD(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.base_profile_nickname_required_field
    ),
    LENGTH_EXCEEDED_ERROR(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.base_profile_nickname_length_exceed_error
    ),
    ALREADY_IN_USE(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.base_profile_nickname_already_in_use
    ),
    AVAILABLE(
        inputState = InputState.DEFAULT,
        guideMessageId = R.string.base_profile_nickname_available
    ),
    DUPLICATE_CHECK_REQUIRED(
        inputState = InputState.WARNIING,
        guideMessageId = R.string.base_profile_nickname_duplicate_check_required
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
