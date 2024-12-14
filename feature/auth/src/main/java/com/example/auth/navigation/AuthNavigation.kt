package com.example.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.auth.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
data object AuthGraph

sealed class AuthGraphDest {
    @Serializable
    data object AuthRoute : AuthGraphDest()
}

fun NavController.navigateToAuth(navOptions: NavOptions) =
    navigate(route = AuthGraphDest.AuthRoute, navOptions)

fun NavGraphBuilder.authNavGraph(
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraph>(startDestination = AuthGraphDest.AuthRoute) {
        composable<AuthGraphDest.AuthRoute> {
            AuthRoute(
                onLoginSuccess = onLoginSuccess
            )
        }
    }
}