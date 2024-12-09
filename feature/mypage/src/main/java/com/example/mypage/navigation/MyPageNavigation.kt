package com.example.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mypage.MyPageRoute
import kotlinx.serialization.Serializable

@Serializable
object MyPageRoute

fun NavController.navigateToMyPage(navOptions: NavOptions) =
    navigate(route = MyPageRoute, navOptions)

fun NavGraphBuilder.myPageScreen() {
    composable<MyPageRoute> {
        MyPageRoute()
    }
}
