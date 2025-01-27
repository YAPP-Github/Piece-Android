package com.puzzle.setting.graph.withdraw.contract

import com.puzzle.navigation.NavigationEvent

sealed class WithdrawSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : WithdrawSideEffect()
}
