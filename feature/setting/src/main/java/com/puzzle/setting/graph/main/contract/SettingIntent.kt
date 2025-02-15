package com.puzzle.setting.graph.main.contract

sealed class SettingIntent {
    data object OnWithdrawClick : SettingIntent()
    data object OnLogoutClick : SettingIntent()
    data object OnNoticeClick : SettingIntent()
    data object OnPrivacyAndPolicyClick : SettingIntent()
    data object OnTermsOfUseClick : SettingIntent()
    data object OnInquiryClick : SettingIntent()
    data object UpdatePushNotification : SettingIntent()
    data object UpdateMatchNotification : SettingIntent()
    data object UpdateBlockAcquaintances : SettingIntent()
    data class OnRefreshClick(val phoneNumbers: List<String>) : SettingIntent()
}
