package com.puzzle.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationHelper @Inject constructor() {
    private val _navigationFlow = Channel<NavigationEvent>(BUFFERED)
    val navigationFlow = _navigationFlow.receiveAsFlow()

    fun navigate(navigationEvent: NavigationEvent) {
        _navigationFlow.trySend(navigationEvent)
    }
}

sealed class NavigationEvent {
    data class NavigateTo(val route: Route, val popUpTo: Route? = null) : NavigationEvent()
    data object NavigateUp : NavigationEvent()
    data class TopLevelNavigateTo(val route: Route) : NavigationEvent()
}
