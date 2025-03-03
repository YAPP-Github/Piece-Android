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

    @Serializable
    data class WebViewRoute(val title: String, val url: String) : SettingGraphDest()
}

@Serializable
data object MatchingGraph : Route

sealed class MatchingGraphDest : Route {
    @Serializable
    data object MatchingRoute : MatchingGraphDest()

    @Serializable
    data class MatchingDetailRoute(val matchId: Int) : MatchingGraphDest()

    @Serializable
    data class ReportRoute(val matchId: Int, val userName: String) : MatchingGraphDest()

    @Serializable
    data class BlockRoute(val matchId: Int, val userName: String) : MatchingGraphDest()

    @Serializable
    data object ContactRoute : MatchingGraphDest()

    @Serializable
    data object ProfilePreviewRoute : MatchingGraphDest()
}

@Serializable
data object ProfileGraph : Route

sealed class ProfileGraphDest : Route {
    @Serializable
    data object RegisterProfileRoute : Route

    @Serializable
    data object MainProfileRoute : Route

    @Serializable
    data object ValueTalkProfileRoute : Route

    @Serializable
    data object ValuePickProfileRoute : Route

    @Serializable
    data object BasicProfileRoute : Route
}

@Serializable
data object OnboardingRoute : Route

fun getRouteClassName(route: String?): String? = route?.substringAfterLast(".")
