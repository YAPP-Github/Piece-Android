package com.puzzle.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.navigation.ProfileGraph
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.profile.graph.basic.BasicProfileRoute
import com.puzzle.profile.graph.main.MainProfileRoute
import com.puzzle.profile.graph.register.RegisterProfileRoute
import com.puzzle.profile.graph.valuepick.ValuePickRoute
import com.puzzle.profile.graph.valuetalk.ValueTalkRoute

fun NavGraphBuilder.profileNavGraph() {
    navigation<ProfileGraph>(startDestination = ProfileGraphDest.RegisterProfileRoute) {
        composable<ProfileGraphDest.MainProfileRoute> {
            MainProfileRoute()
        }

        composable<ProfileGraphDest.ValueTalkProfileRoute> {
            ValueTalkRoute()
        }

        composable<ProfileGraphDest.ValuePickProfileRoute> {
            ValuePickRoute()
        }

        composable<ProfileGraphDest.BasicProfileRoute> {
            BasicProfileRoute()
        }

        composable<ProfileGraphDest.RegisterProfileRoute> {
            RegisterProfileRoute()
        }
    }
}
