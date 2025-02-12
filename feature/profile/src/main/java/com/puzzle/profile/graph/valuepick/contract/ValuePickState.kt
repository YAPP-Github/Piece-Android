package com.puzzle.profile.graph.valuepick.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.MyValuePick

data class ValuePickState(
    val screenState: ScreenState = ScreenState.NORMAL,
    val valuePicks: List<MyValuePick> = emptyList(),
) : MavericksState {

    enum class ScreenState {
        EDITING,
        NORMAL
    }
}
