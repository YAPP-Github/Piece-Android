package com.puzzle.profile.graph.register.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.user.UserRole
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO
import com.puzzle.profile.graph.register.model.ValueTalkRegisterRO

data class RegisterProfileState(
    val userRole: UserRole = UserRole.REGISTER,
    val currentPage: Page = Page.BASIC_PROFILE,
    val imageUrl: String? = null,
    val imageUrlInputState: InputState = InputState.DEFAULT,
    val nickname: String = "",
    val isCheckingButtonEnabled: Boolean = false,
    val nickNameGuideMessage: NickNameGuideMessage = NickNameGuideMessage.LENGTH_GUIDE,
    val description: String = "",
    val descriptionInputState: InputState = InputState.DEFAULT,
    val birthdate: String = "",
    val birthdateInputState: InputState = InputState.DEFAULT,
    val location: String = "",
    val locationInputState: InputState = InputState.DEFAULT,
    val height: String = "",
    val heightInputState: InputState = InputState.DEFAULT,
    val weight: String = "",
    val weightInputState: InputState = InputState.DEFAULT,
    val job: String = "",
    val jobInputState: InputState = InputState.DEFAULT,
    val isSmoke: Boolean? = null,
    val isSmokeInputState: InputState = InputState.DEFAULT,
    val isSnsActive: Boolean? = null,
    val isSnsActiveInputState: InputState = InputState.DEFAULT,
    val contacts: List<Contact> = listOf(
        Contact(
            type = ContactType.KAKAO_TALK_ID,
            content = "",
        )
    ),
    val contactsInputState: InputState = InputState.DEFAULT,
    val valuePicks: List<ValuePickRegisterRO> = emptyList(),
    val valueTalks: List<ValueTalkRegisterRO> = emptyList(),
) : MavericksState {

    val isValuePickComplete: Boolean =
        valuePicks.find { it.selectedAnswer == null } == null

    val isValueTalkComplete: Boolean =
        valueTalks.find { it.answer.isBlank() } == null

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
            contactsInputState == InputState.WARNIING ||
            (nickNameGuideMessage != NickNameGuideMessage.AVAILABLE
                    && nickNameGuideMessage != NickNameGuideMessage.DEFAULT)

    val nickNameStateInSavingProfile: NickNameGuideMessage =
        when {
            nickNameGuideMessage == NickNameGuideMessage.AVAILABLE -> nickNameGuideMessage
            nickNameGuideMessage == NickNameGuideMessage.DEFAULT -> nickNameGuideMessage
            nickname.isBlank() -> NickNameGuideMessage.REQUIRED_FIELD
            nickNameGuideMessage == NickNameGuideMessage.LENGTH_GUIDE -> NickNameGuideMessage.DUPLICATE_CHECK_REQUIRED
            else -> nickNameGuideMessage
        }

    enum class Location(val displayName: String) {
        SEOUL("서울특별시"),
        GYEONGGI("경기도"),
        BUSAN("부산광역시"),
        DAEGU("대구광역시"),
        INCHEON("인천광역시"),
        GWANGJU("광주광역시"),
        DAEJEON("대전광역시"),
        ULSAN("울산광역시"),
        SEJONG("세종특별자치시"),
        GANGWON("강원도"),
        CHUNGBUK("충청북도"),
        CHUNGNAM("충청남도"),
        JEONBUK("전라북도"),
        JEONNAM("전라남도"),
        GYEONGBUK("경상북도"),
        GYEONGNAM("경상남도"),
        JEJU("제주특별자치도"),
        OTHER("기타");
    }

    enum class Job(val displayName: String) {
        STUDENT("학생"),
        EMPLOYEE("직장인"),
        EXPERT("전문직"),
        CIVIL_SERVANT("공무원"),
        BUSINESSMAN("사업가"),
        FREELANCER("프리랜서"),
        OTHER("기타");
    }

    enum class Page(val title: String) {
        BASIC_PROFILE(title = ""),
        VALUE_TALK(title = "가치관 Talk"),
        VALUE_PICK(title = "가치관 Pick"),
        SUMMATION(title = ""),
        FINISH(title = "")
        ;

        fun getBottomButtonTextId(): Int? =
            when (this) {
                BASIC_PROFILE -> R.string.next
                VALUE_TALK -> R.string.next
                VALUE_PICK -> R.string.generate_profile
                FINISH -> R.string.check_my_profile
                SUMMATION -> null
            }

        fun getNextPage(): Page? =
            when (this) {
                BASIC_PROFILE -> VALUE_TALK
                VALUE_TALK -> VALUE_PICK
                VALUE_PICK -> SUMMATION
                SUMMATION -> FINISH
                else -> null
            }

        fun getPreviousPage(): Page? =
            when (this) {
                FINISH -> VALUE_PICK
                VALUE_PICK -> VALUE_TALK
                VALUE_TALK -> BASIC_PROFILE
                else -> null
            }

        companion object {
            const val TEXT_DISPLAY_DURATION = 3000L
            const val PAGE_TRANSITION_DURATION = 1000
        }
    }
}
