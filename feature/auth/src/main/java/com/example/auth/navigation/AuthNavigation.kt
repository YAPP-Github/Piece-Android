package com.example.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.auth.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
object AuthRoute

fun NavController.navigateToAuth(navOptions: NavOptions) =
    navigate(route = AuthRoute, navOptions)

fun NavGraphBuilder.authScreen(
    onLoginSuccess: () -> Unit
) {
    composable<AuthRoute> {
        AuthRoute(
            onLoginSuccess = onLoginSuccess
        )
    }
}
