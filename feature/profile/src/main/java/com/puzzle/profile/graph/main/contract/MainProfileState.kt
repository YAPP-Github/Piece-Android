package com.puzzle.profile.graph.main.contract

import com.airbnb.mvrx.MavericksState

data class MainProfileState(
    val isNotificationEnabled: Boolean = false,
    val selfDescription: String = "",
    val nickName: String = "",
    val age: Int = 0,
    val birthYear: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val location: String = "",
    val job: String = "",
    val smokingStatus: String = "",
) : MavericksState
