package com.puzzle.setting.graph.main.contract

import com.airbnb.mvrx.MavericksState

data class SettingState(
    val isLoading: Boolean = false,
) : MavericksState
