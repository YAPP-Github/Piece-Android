package com.puzzle.presentation.navigation

import androidx.annotation.DrawableRes
import com.puzzle.designsystem.R
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.MyPageRoute
import com.puzzle.navigation.SettingRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @DrawableRes val iconDrawableId: Int,
    val contentDescription: String,
    val title: String,
    val route: KClass<*>,
) {
    MY_PAGE(
        iconDrawableId = R.drawable.ic_profile,
        contentDescription = "프로필",
        title = "프로필",
        route = MyPageRoute::class,
    ),
    MATCHING(
        iconDrawableId = R.drawable.ic_profile,
        contentDescription = "매칭",
        title = "매칭",
        route = MatchingGraphDest.MatchingRoute::class,
    ),
    SETTING(
        iconDrawableId = R.drawable.ic_setting,
        contentDescription = "설정",
        title = "설정",
        route = SettingRoute::class,
    );

    companion object {
        val topLevelDestinations = entries
    }
}
