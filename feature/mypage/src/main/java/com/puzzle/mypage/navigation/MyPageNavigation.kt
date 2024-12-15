package com.puzzle.mypage.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.puzzle.mypage.MyPageRoute
import kotlinx.serialization.Serializable

@Serializable
object MyPageRoute

fun NavController.navigateToMyPage(navOptions: NavOptions) =
    navigate(route = MyPageRoute, navOptions)

fun NavGraphBuilder.myPageScreen(modifier: Modifier = Modifier) {
    composable<MyPageRoute> {
        MyPageRoute(modifier)
    }
}
