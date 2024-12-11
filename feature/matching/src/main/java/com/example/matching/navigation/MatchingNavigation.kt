package com.example.matching.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.matching.MatchingRoute
import kotlinx.serialization.Serializable


@Serializable
data object MatchingGraph

sealed class MatchingGraphDest {
    @Serializable
    data object MatchingRoute : MatchingGraphDest()
}

fun NavController.navigateToMatching(navOptions: NavOptions) =
    navigate(route = MatchingGraphDest.MatchingRoute, navOptions)

fun NavGraphBuilder.matchingNavGraph() {
    navigation<MatchingGraph>(startDestination = MatchingGraphDest.MatchingRoute) {
        composable<MatchingGraphDest.MatchingRoute> {
            MatchingRoute()
        }
    }
}