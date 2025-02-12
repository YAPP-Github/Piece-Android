package com.puzzle.matching.graph.preview.contract

import com.puzzle.navigation.NavigationEvent

sealed class ProfilePreviewSideEffect {
    data class Navigate(val navigationEvent: NavigationEvent) : ProfilePreviewSideEffect()
}