package com.puzzle.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.profile.ProfileRoute

fun NavGraphBuilder.profileScreen() {
    composable<ProfileGraphDest.ProfileRoute> {
        ProfileRoute()
    }
}
