package com.puzzle.common.ui

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
class NoRippleInteractionSource : MutableInteractionSource {
    override suspend fun emit(interaction: Interaction) {}
    override val interactions: Flow<Interaction> = emptyFlow()
    override fun tryEmit(interaction: Interaction): Boolean = true
}