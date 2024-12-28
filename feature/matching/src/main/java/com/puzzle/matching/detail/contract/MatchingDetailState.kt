package com.puzzle.matching.detail.contract

import com.airbnb.mvrx.MavericksState

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BasicInfoState,
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
    // ValueTalkState
    val talkCards: List<ValueTalkCard> = emptyList(),
    // ValuePickState
    val pickCards: List<ValuePickCard> = emptyList(),
) : MavericksState {

    enum class MatchingDetailPage(val title: String) {
        BasicInfoState(title = ""),
        ValueTalkState(title = "가치관 Talk"),
        ValuePickState(title = "가치관 Pick");

        companion object {
            fun getNextPage(currentPage: MatchingDetailPage): MatchingDetailPage {
                return when (currentPage) {
                    BasicInfoState -> ValueTalkState
                    ValueTalkState -> ValuePickState
                    ValuePickState -> ValuePickState
                }
            }

            fun getPreviousPage(currentPage: MatchingDetailPage): MatchingDetailPage {
                return when (currentPage) {
                    BasicInfoState -> BasicInfoState
                    ValueTalkState -> BasicInfoState
                    ValuePickState -> ValueTalkState
                }
            }
        }
    }

    data class ValueTalkCard(
        val label: String = "",
        val title: String = "",
        val content: String = "",
    )

    data class ValuePickCard(
        val category: String = "",
        val question: String = "",
        val option1: String = "",
        val option2: String = "",
        val isSimilarToMe: Boolean = true,
    )
}