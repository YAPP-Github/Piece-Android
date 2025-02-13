package com.puzzle.profile.graph.main.contract

import com.airbnb.mvrx.MavericksState

data class MainProfileState(
    val selfDescription: String = "음악과 요리를 좋아하는",
    val nickName: String = "수줍은 수달",
    val age: String = "25",
    val birthYear: String = "00",
    val height: String = "180",
    val weight: String = "72",
    val location: String = "서울특별시",
    val job: String = "프리랜서",
    val smokingStatus: String = "비흡연",
) : MavericksState