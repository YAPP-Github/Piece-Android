package com.puzzle.piece.navigation.home

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.puzzle.etc.navigation.etcScreen
import com.puzzle.matching.navigation.MatchingGraph
import com.puzzle.matching.navigation.matchingNavGraph
import com.puzzle.mypage.navigation.myPageScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeGraph

fun NavGraphBuilder.homeNavGraph(
    onNavigateToDetail: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    navigation<HomeGraph>(startDestination = MatchingGraph) {
        matchingNavGraph(
            onNavigateToDetail = onNavigateToDetail,
            onBack = onBack,
            modifier = modifier,
        )
        myPageScreen(modifier)
        etcScreen(modifier)
    }
}