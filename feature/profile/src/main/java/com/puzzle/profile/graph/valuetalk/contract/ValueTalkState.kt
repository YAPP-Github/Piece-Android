package com.puzzle.profile.graph.valuetalk.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.MyValueTalk

data class ValueTalkState(
    var screenState: ScreenState = ScreenState.NORMAL,
    val valueTalks: List<MyValueTalk> = emptyList(),
) : MavericksState {

    companion object {
        const val TEXT_DISPLAY_DURATION = 3000L
        const val PAGE_TRANSITION_DURATION = 1000
    }

    enum class ScreenState {
        EDITING,
        NORMAL
    }
}
