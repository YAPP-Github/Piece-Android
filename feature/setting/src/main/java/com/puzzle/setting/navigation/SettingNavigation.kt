package com.puzzle.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puzzle.navigation.SettingRoute
import com.puzzle.setting.SettingRoute

fun NavGraphBuilder.settingScreen() {
    composable<SettingRoute> {
        SettingRoute()
    }
}
