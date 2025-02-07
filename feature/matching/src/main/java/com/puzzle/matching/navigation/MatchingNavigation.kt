package com.puzzle.matching.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.matching.graph.block.BlockRoute
import com.puzzle.matching.graph.detail.MatchingDetailRoute
import com.puzzle.matching.graph.main.MatchingRoute
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

        composable<MatchingGraphDest.ReportRoute> {
            ReportRoute()
        }

        composable<MatchingGraphDest.BlockRoute> {
            BlockRoute()
        }
    }
}
