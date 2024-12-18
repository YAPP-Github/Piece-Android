package com.puzzle.matching.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.matching.MatchingRoute
import com.puzzle.matching.detail.MatchingDetailRoute
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.MatchingGraphDest

fun NavGraphBuilder.matchingNavGraph() {
    navigation<MatchingGraph>(
        startDestination = MatchingGraphDest.MatchingRoute,
    ) {
        /** 예시 코드, 삭제 예정 */
        composable<MatchingGraphDest.MatchingRoute> {
            MatchingRoute()
        }

        composable<MatchingGraphDest.MatchingDetailRoute> {
            MatchingDetailRoute()
        }
    }
}