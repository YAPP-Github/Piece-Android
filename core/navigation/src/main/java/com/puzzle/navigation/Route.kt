package com.puzzle.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
data object AuthGraph : Route

sealed class AuthGraphDest : Route {
    @Serializable
    data object AuthRoute : AuthGraphDest()
}

@Serializable
data object SettingRoute : Route

@Serializable
data object MatchingGraph : Route

sealed class MatchingGraphDest : Route {
    @Serializable
    data object MatchingRoute : MatchingGraphDest()

    @Serializable
    data object MatchingDetailRoute : MatchingGraphDest()
}

@Serializable
data object MyPageRoute : Route