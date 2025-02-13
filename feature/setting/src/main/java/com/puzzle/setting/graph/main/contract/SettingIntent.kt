package com.puzzle.setting.graph.main.contract

sealed class SettingIntent {
    data object OnWithdrawClick : SettingIntent()
    data object OnLogoutClick : SettingIntent()
    data object OnNoticeClick : SettingIntent()
    data object OnPrivacyAndPolicyClick : SettingIntent()
    data object OnTermsOfUseClick : SettingIntent()
    data object OnInquiryClick : SettingIntent()
}
