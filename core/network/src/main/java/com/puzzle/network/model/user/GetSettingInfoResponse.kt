package com.puzzle.network.model.user

import com.puzzle.domain.model.user.UserSetting
import kotlinx.serialization.Serializable

@Serializable
data class GetSettingInfoResponse(
    val isNotificationEnabled: Boolean?,
    val isMatchNotificationEnabled: Boolean?,
    val isAcquaintanceBlockEnabled: Boolean?,
) {
    fun toDomain() = UserSetting(
        isNotificationEnabled = isNotificationEnabled ?: false,
        isMatchNotificationEnabled = isMatchNotificationEnabled ?: false,
        isAcquaintanceBlockEnabled = isAcquaintanceBlockEnabled ?: false,
    )
}
