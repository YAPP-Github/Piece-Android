package com.puzzle.profile.graph.register.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform

data class RegisterProfileState(
    val nickName: String = "",
    val describeMySelf: String = "",
    val birthday: String = "",
    val region: String = "",
    val height: String = "",
    val weight: String = "",
    val job: String = "",
    val isSmoke: Boolean? = null,
    val isSnsActivity: Boolean? = null,
    val contacts: List<Contact> = listOf(
        Contact(
            snsPlatForm = SnsPlatform.KAKAO_TALK,
            content = "",
        ),
    ),
) : MavericksState {}
