package com.puzzle.profile.graph.basic.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform

data class BasicProfileState(
    val profileScreenState: ScreenState = ScreenState.SAVED,
    val nickName: String = "수줍은 수달",
    val isCheckingButtonEnabled: Boolean = false,
    val nickNameGuideMessage: NickNameGuideMessage = NickNameGuideMessage.DEFAULT,
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
            nickName.isBlank() -> NickNameGuideMessage.NEEDS_TO_FILL

            nickName.length in 1..6 &&
                    nickNameGuideMessage != NickNameGuideMessage.AVAILABLE ->
                NickNameGuideMessage.NEEDS_DUPLICATE_CHECK

            nickNameGuideMessage == NickNameGuideMessage.DEFAULT ->  nickNameGuideMessage

            else ->
                nickNameGuideMessage
        }

    enum class ScreenState {
        EDITING,
        SAVED,
        SAVE_FAILED,
    }

    enum class NickNameGuideMessage(val inputState: InputState) {
        DEFAULT(inputState = InputState.DEFAULT),              // 기본 문구
        NEEDS_TO_FILL(inputState = InputState.WARNIING),      // 글자 수 0
        EXCEEDS_MAX_LENGTH(inputState = InputState.WARNIING),  // 글자 수 7 이상
        ALREADY_IN_USE(inputState = InputState.WARNIING),     // API 결과: 중복
        AVAILABLE(inputState = InputState.DEFAULT),           // API 결과: 사용 가능
        NEEDS_DUPLICATE_CHECK(inputState = InputState.WARNIING), // 중복 검사 안하고 저장 버튼 눌렀을 때
    }

    enum class InputState {
        DEFAULT,
        WARNIING,
        ;

        companion object {
            fun getInputState(fieldValue: String): InputState =
                if (fieldValue.isBlank()) BasicProfileState.InputState.WARNIING
                else BasicProfileState.InputState.DEFAULT
        }
    }
}