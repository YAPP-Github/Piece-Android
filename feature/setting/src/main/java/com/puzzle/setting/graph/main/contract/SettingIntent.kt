package com.puzzle.setting.graph.main.contract

sealed class SettingIntent {
    data object OnWithdrawClick : SettingIntent()
    data object OnLogoutClick : SettingIntent()
}
