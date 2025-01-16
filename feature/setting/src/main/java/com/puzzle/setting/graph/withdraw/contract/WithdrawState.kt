package com.puzzle.setting.graph.withdraw.contract

import com.airbnb.mvrx.MavericksState

data class WithdrawState(
    val isLoading: Boolean = false,
) : MavericksState
