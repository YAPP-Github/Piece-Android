package com.puzzle.piece.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.puzzle.auth.navigation.authNavGraph
import com.puzzle.setting.navigation.settingScreen
import com.puzzle.matching.navigation.matchingNavGraph
import com.puzzle.mypage.navigation.myPageScreen
import com.puzzle.navigation.AuthGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AuthGraph,
        modifier = modifier,
    ) {
        authNavGraph()
        matchingNavGraph()
        myPageScreen()
        settingScreen()
    }
}
