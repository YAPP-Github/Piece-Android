package com.example.matching.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.matching.MatchingRoute
import kotlinx.serialization.Serializable

@Serializable
object MatchingRoute

fun NavController.navigateToMatching(navOptions: NavOptions) =
    navigate(route = MatchingRoute, navOptions)

fun NavGraphBuilder.matchingScreen() {
    composable<MatchingRoute> {
        MatchingRoute()
    }
}
