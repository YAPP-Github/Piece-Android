package com.puzzle.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.puzzle.auth.graph.login.LoginRoute
import com.puzzle.auth.graph.signup.SignUpRoute
import com.puzzle.auth.graph.verification.VerificationRoute
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest

fun NavGraphBuilder.authNavGraph() {
    navigation<AuthGraph>(startDestination = AuthGraphDest.SignUpRoute) {
        composable<AuthGraphDest.LoginRoute> {
            LoginRoute()
        }

        composable<AuthGraphDest.SignUpRoute> {
            SignUpRoute()
        }

        composable<AuthGraphDest.VerificationRoute> {
            VerificationRoute()
        }
    }
}
