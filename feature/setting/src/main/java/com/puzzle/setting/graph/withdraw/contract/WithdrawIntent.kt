package com.puzzle.setting.graph.withdraw.contract

import com.puzzle.navigation.NavigationEvent

sealed class WithdrawIntent {
    data object OnSameReasonClick : WithdrawIntent()
    data class OnReasonsClick(val withdrawReason: WithdrawState.WithdrawReason) :
        WithdrawIntent()

    data object OnWithdrawClick : WithdrawIntent()
    data object OnNextClick : WithdrawIntent()
    data class Navigate(val navigationEvent: NavigationEvent) : WithdrawIntent()
}