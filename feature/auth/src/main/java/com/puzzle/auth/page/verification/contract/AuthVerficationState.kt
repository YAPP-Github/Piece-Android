package com.puzzle.auth.page.verification.contract

import com.airbnb.mvrx.MavericksState

data class AuthVerficationState(
    val a: Boolean,
) : MavericksState