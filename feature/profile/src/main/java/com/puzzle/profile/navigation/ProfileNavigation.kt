package com.puzzle.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.navigation.ProfileGraph
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.profile.graph.main.MainProfileRoute
import com.puzzle.profile.graph.register.RegisterProfileRoute

fun NavGraphBuilder.profileNavGraph() {
    navigation<ProfileGraph>(startDestination = ProfileGraphDest.ProfileRoute) {
        composable<ProfileGraphDest.ProfileRoute> {
            MainProfileRoute()
        }

        composable<ProfileGraphDest.RegisterProfileRoute> {
            RegisterProfileRoute()
        }
    }
}
