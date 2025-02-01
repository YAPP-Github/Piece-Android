package com.puzzle.profile.graph.valuepick.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.matching.ValuePick

data class ValuePickState(
    val valuePicks: List<ValuePick> = emptyList()
) : MavericksState {

    enum class ScreenState {
        EDITING,
        SAVED
    }
}