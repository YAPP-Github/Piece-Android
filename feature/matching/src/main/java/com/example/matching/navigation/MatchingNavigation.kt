package com.example.matching.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.matching.MatchingDetailRoute
import com.example.matching.MatchingRoute
import kotlinx.serialization.Serializable


@Serializable
data object MatchingGraph

sealed class MatchingGraphDest {
    @Serializable
    data object MatchingRoute : MatchingGraphDest()

    @Serializable
    data object MatchingDetailRoute : MatchingGraphDest()
}

fun NavController.navigateToMatching(navOptions: NavOptions) =
    navigate(route = MatchingGraph, navOptions)

fun NavGraphBuilder.matchingNavGraph(
    onNavigateToDetail: () -> Unit,
    onBack: () -> Unit,
) {
    navigation<MatchingGraph>(
        startDestination = MatchingGraphDest.MatchingRoute,
    ) {
        /** 예시 코드, 삭제 예정 */
        composable<MatchingGraphDest.MatchingRoute> {
            MatchingRoute(
                onNavigateToDetail = onNavigateToDetail,
            )
        }

        composable<MatchingGraphDest.MatchingDetailRoute> {
            MatchingDetailRoute(
                onBack = onBack,
            )
        }
    }
}