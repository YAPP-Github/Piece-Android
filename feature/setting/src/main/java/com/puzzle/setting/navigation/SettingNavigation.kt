package com.puzzle.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.puzzle.navigation.SettingGraph
import com.puzzle.navigation.SettingGraphDest
import com.puzzle.setting.graph.main.SettingRoute
import com.puzzle.setting.graph.webview.WebViewRoute
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

        composable<SettingGraphDest.WebViewRoute> { backStackEntry ->
            val webView = backStackEntry.toRoute<SettingGraphDest.WebViewRoute>()
            WebViewRoute(
                title = webView.title,
                url = webView.url,
            )
        }
    }
}
