package com.puzzle.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.auth.graph.main.AuthRoute
import com.puzzle.auth.graph.registration.AuthRegistrationRoute
import com.puzzle.auth.graph.verification.AuthVerificationRoute
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest

fun NavGraphBuilder.authNavGraph() {
    navigation<AuthGraph>(startDestination = AuthGraphDest.AuthRoute) {
        composable<AuthGraphDest.AuthRoute> {
            AuthRoute()
        }

        composable<AuthGraphDest.AuthRegistrationRoute> {
            AuthRegistrationRoute()
        }

        composable<AuthGraphDest.AuthVerificationRoute> {
            AuthVerificationRoute()
        }
    }
}