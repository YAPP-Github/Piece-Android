package com.puzzle.matching.graph.detail.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.ValuePickQuestion
import com.puzzle.domain.model.profile.ValueTalkQuestion

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BasicInfoPage,
    val selfDescription: String = "음악과 요리를 좋아하는",
    val nickName: String = "수줍은 수달",
    val age: String = "25",
    val birthYear: String = "00",
    val height: String = "254",
    val occupation: String = "개발자",
    val activityRegion: String = "서울특별시",
    val smokeStatue: String = "비흡연",
    val talkCards: List<ValueTalkQuestion> = emptyList(),
    val pickCards: List<ValuePickQuestion> = emptyList(),
    val imageUri: String = "",
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
