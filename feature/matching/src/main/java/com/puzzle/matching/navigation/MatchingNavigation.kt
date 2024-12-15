package com.puzzle.matching.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.matching.MatchingRoute
import com.puzzle.matching.detail.MatchingDetailRoute
import kotlinx.serialization.Serializable


@Serializable
data object MatchingGraph

sealed class MatchingGraphDest {
    @Serializable
    data object MatchingRoute : MatchingGraphDest()

    @Serializable
    data object MatchingDetailRoute : MatchingGraphDest()
}

fun NavController.navigateToMatchingGraph(navOptions: NavOptions) =
    navigate(route = MatchingGraph, navOptions)

fun NavController.navigateToMatchingDetailRoute() =
    navigate(MatchingGraphDest.MatchingDetailRoute)

fun NavGraphBuilder.matchingNavGraph(
    onNavigateToDetail: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    navigation<MatchingGraph>(
        startDestination = MatchingGraphDest.MatchingRoute,
    ) {
        /** 예시 코드, 삭제 예정 */
        composable<MatchingGraphDest.MatchingRoute> {
            MatchingRoute(
                onNavigateToDetail = onNavigateToDetail,
                modifier = modifier,
            )
        }

        composable<MatchingGraphDest.MatchingDetailRoute> {
            MatchingDetailRoute(
                onBack = onBack,
                modifier = modifier,
            )
        }
    }
}