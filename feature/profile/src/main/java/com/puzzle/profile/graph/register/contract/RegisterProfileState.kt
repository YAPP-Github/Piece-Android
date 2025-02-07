package com.puzzle.profile.graph.register.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Answer
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage

data class RegisterProfileState(
    val profileImageUri: String? = null,
    val profileImageUriInputState: InputState = InputState.DEFAULT,
    val nickName: String = "",
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
    val contacts: List<Contact> = emptyList(),
    val contactsInputState: InputState = InputState.DEFAULT,
    val valuePicks: List<ValuePick> = listOf(
        ValuePick(
            id = 0,
            category = "음주",
            question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?",
            selectedAnswer = 1,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "함께 술을 즐기고 싶어요",
                ),
                Answer(
                    number = 2,
                    content = "같이 술을 즐길 수 없어도 괜찮아요."
                )
            ),
        )
    ),
    val valueTalks: List<ValueTalk> = listOf(
        ValueTalk(
            category = "연애관",
            title = "어떠한 사람과 어떠한 연애를 하고 싶은지 들려주세요",
            answer = "저는 연애에서 서로의 존중과 신뢰가 가장 중요하다고 생각합니다. 진정한 소통을 통해 서로의 감정을 이해하고, 함께 성장할 수 있는 관계를 원합니다. 일상 속 작은 것에도 감사하며, 서로의 꿈과 목표를 지지하고 응원하는 파트너가 되고 싶습니다. 또한, 유머와 즐거움을 잃지 않으며, 함께하는 순간들을 소중히 여기고 싶습니다. 사랑은 서로를 더 나은 사람으로 만들어주는 힘이 있다고 믿습니다. 서로에게 긍정적인 영향을 주며 행복한 시간을 함께하고 싶습니다!",
            summary = "신뢰하며, 함께 성장하고 싶어요.",
            helpMessages = listOf(
                "함께 하고 싶은 데이트 스타일은 무엇인가요?",
                "이상적인 관계의 모습을 적어 보세요",
                "연인과 함께 만들고 싶은 추억이 있나요?",
                "연애에서 가장 중요시하는 가치는 무엇인가요?",
                "연인 관계를 통해 어떤 가치를 얻고 싶나요?",
            ),
        )
    ),
) : MavericksState {

    val usingSnsPlatforms = contacts.map { it.snsPlatform }
        .toSet()

    companion object {
        const val TEXT_DISPLAY_DURATION = 3000L
        const val PAGE_TRANSITION_DURATION = 1000
    }

    val isProfileIncomplete: Boolean = description.isBlank() ||
            birthdate.isBlank() ||
            location.isBlank() ||
            height.isBlank() ||
            weight.isBlank() ||
            job.isBlank() ||
            nickName.isBlank() ||
            isSmoke == null ||
            isSnsActive == null ||
            contacts.isEmpty() ||
            contacts.find { it.content.isBlank() } != null ||
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
