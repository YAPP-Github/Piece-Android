package com.puzzle.matching.graph.detail.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.OpponentValuePick
import com.puzzle.domain.model.profile.OpponentValueTalk

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BasicInfoPage,
    val description: String = "",
    val nickname: String = "",
    val age: String = "",
    val birthYear: String = "",
    val height: String = "",
    val weight: String = "",
    val job: String = "",
    val location: String = "",
    val smokeStatue: String = "",
    val valueTalks: List<OpponentValueTalk> = emptyList(),
    val valuePicks: List<OpponentValuePick> = emptyList(),
    val imageUrl: String = "",
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
