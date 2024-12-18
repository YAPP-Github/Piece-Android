package com.puzzle.etc.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puzzle.etc.EtcRoute
import com.puzzle.navigation.EtcRoute

fun NavGraphBuilder.etcScreen() {
    composable<EtcRoute> {
        EtcRoute()
    }
}
