package com.puzzle.profile.graph.register.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Answer
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ValuePick
import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage

data class RegisterProfileState(
    val currentPage: Page = Page.BASIC_PROFILE,
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
            selectedAnswer = 0,
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
        ),
        ValuePick(
            id = 1,
            category = "만남 빈도",
            question = "주말에 얼마나 자주 데이트를 하고싶나요?",
            selectedAnswer = 0,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "주말에는 최대한 같이 있고 싶어요",
                ),
                Answer(
                    number = 2,
                    content = "하루 정도는 각자 보내고 싶어요."
                )
            ),
        ),
        ValuePick(
            id = 2,
            category = "연락 빈도",
            question = "연인 사이에 얼마나 자주 연락하는게 좋은가요?",
            selectedAnswer = 0,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "바빠도 최대한 자주 연락하고 싶어요",
                ),
                Answer(
                    number = 2,
                    content = "연락은 생각날 때만 종종 해도 괜찮아요"
                )
            ),
        ),
        ValuePick(
            id = 3,
            category = "연락 방식",
            question = "연락할 때 어떤 방법을 더 좋아하나요?",
            selectedAnswer = 0,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "전화보다는 문자나 카톡이 좋아요",
                ),
                Answer(
                    number = 2,
                    content = "문자나 카톡보다는 전화가 좋아요"
                )
            ),
        ),
        ValuePick(
            id = 4,
            category = "데이트",
            question = "공공장소에서 연인 티를 내는 것에 대해 어떻게 생각하나요?",
            selectedAnswer = 0,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "밖에서 연인 티내는건 조금 부담스러워요",
                ),
                Answer(
                    number = 2,
                    content = "밖에서도 자연스럽게 연인 티내고 싶어요"
                )
            ),
        ),
        ValuePick(
            id = 5,
            category = "장거리 연애",
            question = "장거리 연애에 대해 어떻게 생각하나요?",
            selectedAnswer = 0,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "믿음이 있다면 장거리 연애도 괜찮아요",
                ),
                Answer(
                    number = 2,
                    content = "장거리 연애는 조금 힘들어요"
                )
            ),
        ),
        ValuePick(
            id = 6,
            category = "SNS",
            question = "연인이 활발한 SNS 활동을 하거나 게스타라면 기분이 어떨 것 같나요?",
            selectedAnswer = 0,
            answers = listOf(
                Answer(
                    number = 1,
                    content = "연인의 SNS 활동, 크게 상관 없어요",
                ),
                Answer(
                    number = 2,
                    content = "SNS 활동과 게스타는 조금 불편해요"
                )
            ),
        ),
    ),
    val valueTalks: List<ValueTalk> = listOf(
        ValueTalk(
            category = "연애관",
            title = "어떠한 사람과 어떠한 연애를 하고 싶은지 들려주세요",
            answer = "",
            summary = "",
            guides = listOf(
                "함께 하고 싶은 데이트 스타일은 무엇인가요?",
                "이상적인 관계의 모습을 적어 보세요",
                "연인과 함께 만들고 싶은 추억이 있나요?",
                "연애에서 가장 중요시하는 가치는 무엇인가요?",
                "연인 관계를 통해 어떤 가치를 얻고 싶나요?",
            ),
        ),
        ValueTalk(
            category = "관심사와 취향",
            title = "무엇을 할 때 가장 행복한가요?\n요즘 어떠한 것에 관심을 두고 있나요?",
            answer = "",
            summary = "",
            guides = listOf(
                "당신의 삶을 즐겁게 만드는 것들은 무엇인가요?",
                "일상에서 소소한 행복을 느끼는 순간을 적어보세요",
                "최근에 몰입했던 취미가 있다면 소개해 주세요",
                "최근 마음이 따뜻해졌던 순간을 들려주세요.",
                "요즘 마음을 사로잡은 콘텐츠를 공유해 보세요",
            ),
        ),
        ValueTalk(
            category = "꿈과 목표",
            title = "어떤 일을 하며 무엇을 목표로 살아가나요?\n인생에서 이루고 싶은 꿈은 무엇인가요?",
            answer = "",
            summary = "",
            guides = listOf(
                "당신의 직업은 무엇인가요?",
                "앞으로 하고 싶은 일에 대해 이야기해주세요",
                "어떤 일을 할 때 가장 큰 성취감을 느끼나요?",
                "당신의 버킷리스트를 알려주세요",
                "당신이 꿈꾸는 삶은 어떤 모습인가요?",
            ),
        )
    ),
) : MavericksState {

    val isValuePickComplete: Boolean =
        valuePicks.find { it.selectedAnswer == 0 } == null

    val isValueTalkComplete: Boolean =
        valueTalks.find { it.answer.isBlank() } == null

    val usingSnsPlatforms = contacts.map { it.snsPlatform }
        .toSet()

    val isBasicProfileComplete: Boolean = !profileImageUri.isNullOrBlank() &&
            description.isNotBlank() &&
            birthdate.isNotBlank() &&
            location.isNotBlank() &&
            height.isNotBlank() &&
            weight.isNotBlank() &&
            job.isNotBlank() &&
            nickName.isNotBlank() &&
            isSmoke != null &&
            isSnsActive != null &&
            contacts.isNotEmpty() &&
            contacts.find { it.content.isBlank() } == null &&
            nickNameGuideMessage == NickNameGuideMessage.AVAILABLE

    val updatedNickNameGuideMessage: NickNameGuideMessage =
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
        FINISH(title = "")
        ;

        companion object {
            fun getNextPage(currentPage: Page): Page? =
                when (currentPage) {
                    BASIC_PROFILE -> VALUE_TALK
                    VALUE_TALK -> VALUE_PICK
                    VALUE_PICK -> FINISH
                    else -> null
                }

            fun getPreviousPage(currentPage: Page): Page? =
                when (currentPage) {
                    FINISH -> VALUE_PICK
                    VALUE_PICK -> VALUE_TALK
                    VALUE_TALK -> BASIC_PROFILE
                    else -> null
                }
        }
    }

    companion object {
        const val TEXT_DISPLAY_DURATION = 3000L
        const val PAGE_TRANSITION_DURATION = 1000
    }
}
