package com.puzzle.matching.graph.preview.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk

data class ProfilePreviewState(
    val isLoading: Boolean = false,
    val myProfileBasic: MyProfileBasic? = null,
    val myValuePicks: List<MyValuePick> = emptyList(),
    val myValueTalks: List<MyValueTalk> = emptyList(),
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
