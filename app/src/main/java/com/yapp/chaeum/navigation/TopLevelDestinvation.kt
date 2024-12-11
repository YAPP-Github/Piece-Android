package com.yapp.chaeum.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Call
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.etc.navigation.EtcRoute
import com.example.matching.navigation.MatchingGraphDest
import com.example.mypage.navigation.MyPageRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val titleText: String,
    val route: KClass<*>,
) {
    MATCHING(
        selectedIcon = Icons.Filled.Call,
        unselectedIcon = Icons.Outlined.Call,
        iconText = "매칭",
        titleText = "매칭",
        route = MatchingGraphDest.MatchingRoute::class,
    ),
    MYPAGE(
        selectedIcon = Icons.Filled.Call,
        unselectedIcon = Icons.Outlined.Call,
        iconText = "마이페이지",
        titleText = "마이페이지",
        route = MyPageRoute::class,
    ),
    ETC(
        selectedIcon = Icons.Filled.Call,
        unselectedIcon = Icons.Outlined.Call,
        iconText = "ETC",
        titleText = "ETC",
        route = EtcRoute::class,
    ),
}
