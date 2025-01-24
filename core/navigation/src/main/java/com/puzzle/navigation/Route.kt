package com.puzzle.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
data object AuthGraph : Route

sealed class AuthGraphDest : Route {
    @Serializable
    data object LoginRoute : AuthGraphDest()

    @Serializable
    data object VerificationRoute : AuthGraphDest()

    @Serializable
    data object SignUpRoute : AuthGraphDest()
}

@Serializable
data object SettingGraph : Route

sealed class SettingGraphDest : Route {
    @Serializable
    data object SettingRoute : SettingGraphDest()

    @Serializable
    data object WithdrawRoute : SettingGraphDest()
}

@Serializable
data object MatchingGraph : Route

sealed class MatchingGraphDest : Route {
    @Serializable
    data object MatchingRoute : MatchingGraphDest()

    @Serializable
    data object MatchingDetailRoute : MatchingGraphDest()
}

@Serializable
data object ProfileGraph : Route

sealed class ProfileGraphDest : Route {
    @Serializable
    data object RegisterProfileRoute : Route

    @Serializable
    data object ProfileRoute : Route
}
