package com.puzzle.profile.graph.valuepick.contract

import com.puzzle.domain.model.profile.MyValuePick

sealed class ValuePickIntent {
    data class OnUpdateButtonClick(val newValuePicks: List<MyValuePick>) : ValuePickIntent()
    data object OnBackClick : ValuePickIntent()
}
