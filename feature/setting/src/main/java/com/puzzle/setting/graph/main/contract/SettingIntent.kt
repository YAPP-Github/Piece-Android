package com.puzzle.setting.graph.main.contract

import com.puzzle.navigation.NavigationEvent

sealed class SettingIntent {
    data class Navigate(val navigationEvent: NavigationEvent) : SettingIntent()
}