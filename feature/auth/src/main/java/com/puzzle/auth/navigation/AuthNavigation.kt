package com.puzzle.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.auth.page.main.AuthRoute
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest

fun NavGraphBuilder.authNavGraph() {
    navigation<AuthGraph>(startDestination = AuthGraphDest.AuthRoute) {
        composable<AuthGraphDest.AuthRoute> {
            AuthRoute()
        }
    }
}