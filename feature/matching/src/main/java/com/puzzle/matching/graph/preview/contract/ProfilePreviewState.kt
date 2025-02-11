package com.puzzle.matching.graph.preview.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.OpponentProfile

data class ProfilePreviewState(
    val isLoading: Boolean = false,
    val profile: OpponentProfile? = null,
) : MavericksState {

    enum class Page(val title: String) {
        BasicInfoPage(title = ""),
        ValueTalkPage(title = "가치관 Talk"),
        ValuePickPage(title = "가치관 Pick"),
        ;

        fun getNextPage(): Page? =
            when (this) {
                BasicInfoPage -> ValueTalkPage
                ValueTalkPage -> ValuePickPage
                ValuePickPage -> null
            }

        fun getPreviousPage(): Page? =
            when (this) {
                BasicInfoPage -> null
                ValueTalkPage -> BasicInfoPage
                ValuePickPage -> ValueTalkPage
            }
    }
}
