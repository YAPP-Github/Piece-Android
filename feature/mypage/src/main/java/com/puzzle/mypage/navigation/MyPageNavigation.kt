package com.puzzle.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puzzle.mypage.MyPageRoute
import com.puzzle.navigation.MyPageRoute

fun NavGraphBuilder.myPageScreen() {
    composable<MyPageRoute> {
        MyPageRoute()
    }
}
