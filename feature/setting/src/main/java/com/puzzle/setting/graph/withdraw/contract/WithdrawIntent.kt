package com.puzzle.setting.graph.withdraw.contract

sealed class WithdrawIntent {
    data class OnReasonsClick(val withdrawReason: WithdrawState.WithdrawReason) :
        WithdrawIntent()

    data class UpdateReason(val reason: String) : WithdrawIntent()
    data object OnWithdrawClick : WithdrawIntent()
    data object OnNextClick : WithdrawIntent()
    data object onBackClick : WithdrawIntent()
}
