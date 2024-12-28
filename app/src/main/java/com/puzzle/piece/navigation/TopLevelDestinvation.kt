package com.puzzle.piece.navigation

import androidx.annotation.DrawableRes
import com.puzzle.navigation.EtcRoute
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.MyPageRoute
import com.puzzle.piece.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @DrawableRes val iconDrawableId: Int,
    val contentDescription: String,
    val title: String,
    val route: KClass<*>,
) {
    MY_PAGE(
        iconDrawableId = R.drawable.ic_profile,
        contentDescription = "마이페이지",
        title = "마이페이지",
        route = MyPageRoute::class,
    ),
    MATCHING(
        iconDrawableId = R.drawable.ic_profile,
        contentDescription = "매칭",
        title = "매칭",
        route = MatchingGraphDest.MatchingRoute::class,
    ),
    ETC(
        iconDrawableId = R.drawable.ic_setting,
        contentDescription = "ETC",
        title = "ETC",
        route = EtcRoute::class,
    );

    companion object {
        val topLevelDestinations = entries
    }
}
