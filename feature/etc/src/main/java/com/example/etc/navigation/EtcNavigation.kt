package com.example.etc.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.etc.EtcRoute
import kotlinx.serialization.Serializable

@Serializable
object EtcRoute

fun NavController.navigateToEtc(navOptions: NavOptions) =
    navigate(route = EtcRoute, navOptions)

fun NavGraphBuilder.etcScreen() {
    composable<EtcRoute> {
        EtcRoute()
    }
}
