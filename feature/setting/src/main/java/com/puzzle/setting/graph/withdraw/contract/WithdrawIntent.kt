package com.puzzle.setting.graph.withdraw.contract

sealed class WithdrawIntent {
    data object OnSameWithdrawReasonClick : WithdrawIntent()
    data class OnWithdrawReasonsClick(val withdrawReason: WithdrawState.WithdrawReason) :
        WithdrawIntent()
}