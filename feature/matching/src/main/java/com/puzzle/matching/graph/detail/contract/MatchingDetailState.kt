package com.puzzle.matching.graph.detail.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.OpponentProfile

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BasicInfoPage,
    val profile: OpponentProfile? = null,
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
