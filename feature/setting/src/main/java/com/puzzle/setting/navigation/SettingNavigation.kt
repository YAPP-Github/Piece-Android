package com.puzzle.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puzzle.setting.SettingRoute
import com.puzzle.navigation.SettingRoute

fun NavGraphBuilder.settingScreen() {
    composable<SettingRoute> {
        SettingRoute()
    }
}
