package com.puzzle.profile.graph.valuetalk.contract

import com.puzzle.domain.model.profile.MyValueTalk

sealed class ValueTalkIntent {
    data class OnUpdateClick(val newValueTalks: List<MyValueTalk>) : ValueTalkIntent()
    data object OnEditClick : ValueTalkIntent()
    data object OnBackClick : ValueTalkIntent()
}
