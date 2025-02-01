package com.puzzle.matching.graph.detail.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.matching.ValuePick
import com.puzzle.domain.model.matching.ValueTalk

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BasicInfoPage,
    // BasicInfoState
    val selfDescription: String = "음악과 요리를 좋아하는",
    val nickName: String = "수줍은 수달",
    val age: String = "25",
    val birthYear: String = "00",
    val height: String = "254",
    val religion: String = "무교",
    val occupation: String = "개발자",
    val activityRegion: String = "서울특별시",
    val smokeStatue: String = "비흡연",
    val talkCards: List<ValueTalk> = listOf(
        ValueTalk(
            category = "꿈과 목표",
            title = "여행하며 문화 경험, LGBTQ+ 변화를 원해요.",
            answer = "안녕하세요! 저는 삶의 매 순간을 소중히 여기며, 꿈과 목표를 이루기 위해 노력하는 사람입니다. 제 가장 큰 꿈은 여행을 통해 다양한 문화와 사람들을 경험하고, 그 과정에서 얻은 지혜를 나누는 것입니다. 또한, LGBTQ+ 커뮤니티를 위한 긍정적인 변화를 이끌어내고 싶습니다. 내가 이루고자 하는 목표는 나 자신을 발전시키고, 사랑하는 사람들과 함께 행복한 순간들을 만드는 것입니다. 서로의 꿈을 지지하며 함께 성장할 수 있는 관계를 기대합니다!",
        ),
        ValueTalk(
            category = "관심사와 취향",
            title = "음악, 요리, 하이킹을 좋아해요.",
            answer = "저는 다양한 취미와 관심사를 가진 사람입니다. 음악을 사랑하여 콘서트에 자주 가고, 특히 인디 음악과 재즈에 매력을 느낍니다. 요리도 좋아해 새로운 레시피에 도전하는 것을 즐깁니다. 여행을 통해 새로운 맛과 문화를 경험하는 것도 큰 기쁨입니다. 또, 자연을 사랑해서 주말마다 하이킹이나 캠핑을 자주 떠납니다. 영화와 책도 좋아해, 좋은 이야기와 감동을 나누는 시간을 소중히 여깁니다. 서로의 취향을 공유하며 즐거운 시간을 보낼 수 있기를 기대합니다!",
        ),
        ValueTalk(
            category = "연애관",
            title = "서로 존중하고 신뢰하며, 함께 성장하는 관계를 원해요. ",
            answer = "저는 연애에서 서로의 존중과 신뢰가 가장 중요하다고 생각합니다. 진정한 소통을 통해 서로의 감정을 이해하고, 함께 성장할 수 있는 관계를 원합니다. 일상 속 작은 것에도 감사하며, 서로의 꿈과 목표를 지지하고 응원하는 파트너가 되고 싶습니다. 또한, 유머와 즐거움을 잃지 않으며, 함께하는 순간들을 소중히 여기고 싶습니다. 사랑은 서로를 더 나은 사람으로 만들어주는 힘이 있다고 믿습니다. 서로에게 긍정적인 영향을 주며 행복한 시간을 함께하고 싶습니다!"
        )
    ),
    val pickCards: List<ValuePick> = listOf(
        ValuePick(
            category = "음주",
            question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?",
            option1 = "함께 술을 즐기고 싶어요",
            option2 = "같이 술을 즐길 수 없어도 괜찮아요",
            isSimilarToMe = true,
        ),
        ValuePick(
            category = "만남 빈도",
            question = "주말에 얼마나 자주 데이트를 하고싶나요?",
            option1 = "주말에는 최대한 같이 있고 싶어요",
            option2 = "하루 정도는 각자 보내고 싶어요",
            isSimilarToMe = false,
        ),
        ValuePick(
            category = "연락 빈도",
            question = "연인 사이에 얼마나 자주 연락하는게 좋은가요?",
            option1 = "바빠도 최대한 자주 연락하고 싶어요",
            option2 = "연락은 생각날 때만 종종 해도 괜찮아요",
            isSimilarToMe = true,
        ),
        ValuePick(
            category = "연락 방식",
            question = "연락할 때 어떤 방법을 더 좋아하나요?",
            option1 = "전화보다는 문자나 카톡이 좋아요",
            option2 = "문자나 카톡보다는 전화가 좋아요",
            isSimilarToMe = false,
        )
    ),
) : MavericksState {

    enum class MatchingDetailPage(val title: String) {
        BasicInfoPage(title = ""),
        ValueTalkPage(title = "가치관 Talk"),
        ValuePickPage(title = "가치관 Pick");

        companion object {
            fun getNextPage(currentPage: MatchingDetailPage): MatchingDetailPage {
                return when (currentPage) {
                    BasicInfoPage -> ValueTalkPage
                    ValueTalkPage -> ValuePickPage
                    ValuePickPage -> ValuePickPage
                }
            }

            fun getPreviousPage(currentPage: MatchingDetailPage): MatchingDetailPage {
                return when (currentPage) {
                    BasicInfoPage -> BasicInfoPage
                    ValueTalkPage -> BasicInfoPage
                    ValuePickPage -> ValueTalkPage
                }
            }
        }
    }
}
