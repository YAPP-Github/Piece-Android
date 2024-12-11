package com.yapp.chaeum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.etc.navigation.etcScreen
import com.example.matching.navigation.MatchingGraph
import com.example.matching.navigation.matchingNavGraph
import com.example.mypage.navigation.myPageScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MatchingGraph,
        modifier = modifier,
    ) {
        matchingNavGraph()
        myPageScreen()
        etcScreen()
    }
}