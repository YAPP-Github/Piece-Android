package com.puzzle.setting.graph.main.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.auth.OAuthProvider

data class SettingState(
    val isLoading: Boolean = false,
    val oAuthProvider: OAuthProvider ?= null,
    val email: String = "",
    val isMatchingNotificationEnabled: Boolean = false,
    val isPushNotificationEnabled: Boolean = false,
    val isContactBlocked: Boolean = false,
    val lastRefreshTime: String = "",
    val version: String = "",
) : MavericksState
