package com.puzzle.setting.graph.withdraw.contract

sealed class WithdrawIntent {
    data object OnSameReasonClick : WithdrawIntent()
    data class OnReasonsClick(val withdrawReason: WithdrawState.WithdrawReason) :
        WithdrawIntent()

    data object OnWithdrawClick : WithdrawIntent()
    data object OnNextClick : WithdrawIntent()
    data object onBackClick : WithdrawIntent()
}