package com.puzzle.profile.graph.main.contract

import com.airbnb.mvrx.MavericksState

data class MainProfileState(
    val a: Int = 0,
) : MavericksState {}