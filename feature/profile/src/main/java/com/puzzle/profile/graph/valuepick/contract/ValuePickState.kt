package com.puzzle.profile.graph.valuepick.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO

data class ValuePickState(
    val valuePicks: List<ValuePickRegisterRO> = emptyList(),
) : MavericksState {

    enum class ScreenState {
        EDITING,
        SAVED
    }
}
