package com.puzzle.matching.detail.contract

import com.airbnb.mvrx.MavericksState

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = BasicInfoState(),
) : MavericksState {

    sealed class MatchingDetailPage(open val title: String)

    data class BasicInfoState(
        override val title: String = "",
        val selfDescription: String = "",
        val nickName: String = "",
        val birthYear: String = "",
        val height: String = "",
        val religion: String = "",
        val occupation: String = "",
        val activityRegion: String = "",
        val smokeStatue: String = "",
    ) : MatchingDetailPage(title)

    data class ValueTalkState(
        override val title: String = "",
        val selfDescription: String = "",
        val nickName: String = "",
        val talkCards: List<Card> = emptyList(),
    ) : MatchingDetailPage(title) {
        data class Card(
            val label: String = "",
            val title: String = "",
            val content: String = "",
        )
    }

    data class ValuePickState(
        override val title: String = "",
        val pickCards: List<Card> = emptyList(),
    ) : MatchingDetailPage(title) {
        data class Card(
            val category: String = "",
            val question: String = "",
            val option1: String = "",
            val option2: String = "",
            val isSimilarToMe: Boolean = true,
        )
    }
}