package com.puzzle.presentation.navigation

import androidx.annotation.DrawableRes
import com.puzzle.designsystem.R
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.ProfileGraphDest
import com.puzzle.navigation.SettingGraph
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @DrawableRes val iconDrawableId: Int,
    val contentDescription: String,
    val title: String,
    val route: KClass<*>,
) {
    PROFILE(
        iconDrawableId = R.drawable.ic_profile,
        contentDescription = "프로필",
        title = "프로필",
        route = ProfileGraphDest.MainProfileRoute::class,
    ),
    MATCHING(
        iconDrawableId = R.drawable.ic_profile,
        contentDescription = "매칭",
        title = "",
        route = MatchingGraphDest.MatchingRoute::class,
    ),
    SETTING(
        iconDrawableId = R.drawable.ic_setting,
        contentDescription = "설정",
        title = "설정",
        route = SettingGraph::class,
    );

    companion object {
        val topLevelDestinations = entries
    }
}
