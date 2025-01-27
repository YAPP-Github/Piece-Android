package com.puzzle.profile.graph.main.contract

import com.airbnb.mvrx.MavericksState

data class MainProfileState(
    val nickName: String = "",
    val selfDescription: String = "",
    val age: String = "",
    val birthYear: String = "",
    val height: String = "",
    val activityRegion: String = "",
    val occupation: String = "",
    val smokeStatue: String = "",
    val weight: String = "",
) : MavericksState