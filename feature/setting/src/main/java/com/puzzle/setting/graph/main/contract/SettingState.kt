package com.puzzle.setting.graph.main.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.auth.OAuthProvider

data class SettingState(
    val oAuthProvider: OAuthProvider? = OAuthProvider.KAKAO,
    val email: String = "example@kakao.com",
    val isMatchingNotificationEnabled: Boolean = false,
    val isPushNotificationEnabled: Boolean = false,
    val isContactBlocked: Boolean = false,
    val lastRefreshTime: String = "YYYY년 MM월 DD일 00:00",
    val isLoadingContactsBlocked: Boolean = false,
    val version: String = "",
) : MavericksState
