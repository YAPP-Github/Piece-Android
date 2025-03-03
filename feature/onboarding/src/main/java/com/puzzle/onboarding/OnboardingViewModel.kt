package com.puzzle.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationHelper
import com.puzzle.onboarding.contract.OnboardingIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    internal val navigationHelper: NavigationHelper,
) : ViewModel() {
    private val intents = Channel<OnboardingIntent>(BUFFERED)

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: OnboardingIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.OnStartClick -> navigationHelper.navigate(
                NavigationEvent.To(
                    route = AuthGraphDest.LoginRoute,
                    popUpTo = true,
                )
            )
        }
    }
}
