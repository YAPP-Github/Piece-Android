package com.puzzle.matching.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.puzzle.matching.graph.block.BlockRoute
import com.puzzle.matching.graph.contact.ContactRoute
import com.puzzle.matching.graph.detail.MatchingDetailRoute
import com.puzzle.matching.graph.main.MatchingRoute
import com.puzzle.matching.graph.preview.ProfilePreviewRoute
import com.puzzle.matching.graph.report.ReportRoute
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MatchingGraphDest

fun NavGraphBuilder.matchingNavGraph() {
    navigation<MatchingGraph>(
        startDestination = MatchingGraphDest.MatchingRoute,
    ) {
        composable<MatchingGraphDest.MatchingRoute> {
            MatchingRoute()
        }

        composable<MatchingGraphDest.MatchingDetailRoute> {
            MatchingDetailRoute()
        }

        composable<MatchingGraphDest.ReportRoute> { backStackEntry ->
            val report = backStackEntry.toRoute<MatchingGraphDest.ReportRoute>()
            ReportRoute(
                userId = report.userId,
                userName = report.userName,
            )
        }

        composable<MatchingGraphDest.BlockRoute> { backStackEntry ->
            val block = backStackEntry.toRoute<MatchingGraphDest.BlockRoute>()
            BlockRoute(
                userId = block.userId,
                userName = block.userName,
            )
        }

        composable<MatchingGraphDest.ContactRoute> { backStackEntry ->
            ContactRoute()
        }

        composable<MatchingGraphDest.ProfilePreviewRoute> { backStackEntry ->
            ProfilePreviewRoute()
        }
    }
}
