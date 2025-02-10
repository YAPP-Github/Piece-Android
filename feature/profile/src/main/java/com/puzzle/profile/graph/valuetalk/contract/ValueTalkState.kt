package com.puzzle.profile.graph.valuetalk.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.ValueTalkQuestion

data class ValueTalkState(
    val valueTalkQuestions: List<ValueTalkQuestion> = listOf(
        ValueTalkQuestion(
            category = "연애관",
            title = "어떠한 사람과 어떠한 연애를 하고 싶은지 들려주세요",
            answer = "저는 연애에서 서로의 존중과 신뢰가 가장 중요하다고 생각합니다. 진정한 소통을 통해 서로의 감정을 이해하고, 함께 성장할 수 있는 관계를 원합니다. 일상 속 작은 것에도 감사하며, 서로의 꿈과 목표를 지지하고 응원하는 파트너가 되고 싶습니다. 또한, 유머와 즐거움을 잃지 않으며, 함께하는 순간들을 소중히 여기고 싶습니다. 사랑은 서로를 더 나은 사람으로 만들어주는 힘이 있다고 믿습니다. 서로에게 긍정적인 영향을 주며 행복한 시간을 함께하고 싶습니다!",
            summary = "신뢰하며, 함께 성장하고 싶어요.",
            guides = listOf(
                "함께 하고 싶은 데이트 스타일은 무엇인가요?",
                "이상적인 관계의 모습을 적어 보세요",
                "연인과 함께 만들고 싶은 추억이 있나요?",
                "연애에서 가장 중요시하는 가치는 무엇인가요?",
                "연인 관계를 통해 어떤 가치를 얻고 싶나요?",
            ),
        ),
        ValueTalkQuestion(
            category = "관심사와 취향",
            title = "무엇을 할 때 가장 행복한가요?\n요즘 어떠한 것에 관심을 두고 있나요?",
            answer = "저는 다양한 취미와 관심사를 가진 사람입니다. 음악을 사랑하여 콘서트에 자주 가고, 특히 인디 음악과 재즈에 매력을 느낍니다. 요리도 좋아해 새로운 레시피에 도전하는 것을 즐깁니다. 여행을 통해 새로운 맛과 문화를 경험하는 것도 큰 기쁨입니다. 또, 자연을 사랑해서 주말마다 하이킹이나 캠핑을 자주 떠납니다. 영화와 책도 좋아해, 좋은 이야기와 감동을 나누는 시간을 소중히 여깁니다. 서로의 취향을 공유하며 즐거운 시간을 보낼 수 있기를 기대합니다!",
            summary = "음악, 요리, 하이킹을 좋아해요.",
            guides = listOf(
                "당신의 삶을 즐겁게 만드는 것들은 무엇인가요?",
                "일상에서 소소한 행복을 느끼는 순간을 적어보세요",
                "최근에 몰입했던 취미가 있다면 소개해 주세요",
                "최근 마음이 따뜻해졌던 순간을 들려주세요.",
                "요즘 마음을 사로잡은 콘텐츠를 공유해 보세요",
            ),
        ),
        ValueTalkQuestion(
            category = "꿈과 목표",
            title = "어떤 일을 하며 무엇을 목표로 살아가나요?\n인생에서 이루고 싶은 꿈은 무엇인가요?",
            answer = "안녕하세요! 저는 삶의 매 순간을 소중히 여기며, 꿈과 목표를 이루기 위해 노력하는 사람입니다. 제 가장 큰 꿈은 여행을 통해 다양한 문화와 사람들을 경험하고, 그 과정에서 얻은 지혜를 나누는 것입니다. 또한, LGBTQ+ 커뮤니티를 위한 긍정적인 변화를 이끌어내고 싶습니다. 내가 이루고자 하는 목표는 나 자신을 발전시키고, 사랑하는 사람들과 함께 행복한 순간들을 만드는 것입니다. 서로의 꿈을 지지하며 함께 성장할 수 있는 관계를 기대합니다!",
            summary = "여행하며 LGBTQ+ 변화를 원해요.",
            guides = listOf(
                "당신의 직업은 무엇인가요?",
                "앞으로 하고 싶은 일에 대해 이야기해주세요",
                "어떤 일을 할 때 가장 큰 성취감을 느끼나요?",
                "당신의 버킷리스트를 알려주세요",
                "당신이 꿈꾸는 삶은 어떤 모습인가요?",
            ),
        )
    )
) : MavericksState {

    companion object {
        const val TEXT_DISPLAY_DURATION = 3000L
        const val PAGE_TRANSITION_DURATION = 1000
    }

    enum class ScreenState {
        EDITING,
        SAVED
    }
}
