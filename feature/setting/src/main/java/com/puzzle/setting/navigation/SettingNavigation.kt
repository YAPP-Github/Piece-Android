package com.puzzle.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.puzzle.navigation.SettingGraph
import com.puzzle.navigation.SettingGraphDest
import com.puzzle.setting.SettingRoute
import com.puzzle.setting.graph.withdraw.WithdrawRoute

fun NavGraphBuilder.settingNavGraph() {
    navigation<SettingGraph>(
        startDestination = SettingGraphDest.SettingRoute,
    ) {
        composable<SettingGraphDest.SettingRoute> {
            SettingRoute()
        }

        composable<SettingGraphDest.WithdrawRoute> {
            WithdrawRoute()
        }
    }
}
