package com.puzzle.domain.model.user

data class UserSetting(
    val isNotificationEnabled: Boolean,
    val isMatchNotificationEnabled: Boolean,
    val isAcquaintanceBlockEnabled: Boolean,
)
