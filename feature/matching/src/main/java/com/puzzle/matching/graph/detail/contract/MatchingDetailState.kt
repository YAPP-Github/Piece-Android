package com.puzzle.matching.graph.detail.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.matching.ValuePick
import com.puzzle.domain.model.matching.ValueTalk

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BasicInfoPage,
    // BasicInfoState
    val selfDescription: String = "",
    val nickName: String = "",
    val age: String = "_",
    val birthYear: String = "",
    val height: String = "",
    val religion: String = "",
    val occupation: String = "",
    val activityRegion: String = "",
    val smokeStatue: String = "",
    val talkCards: List<ValueTalk> = emptyList(),
    val pickCards: List<ValuePick> = emptyList(),
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