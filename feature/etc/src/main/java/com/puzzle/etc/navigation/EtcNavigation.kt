package com.puzzle.etc.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.puzzle.etc.EtcRoute
import kotlinx.serialization.Serializable

@Serializable
object EtcRoute

fun NavController.navigateToEtc(navOptions: NavOptions) =
    navigate(route = EtcRoute, navOptions)

fun NavGraphBuilder.etcScreen(modifier: Modifier = Modifier) {
    composable<EtcRoute> {
        EtcRoute(modifier)
    }
}
