package com.puzzle.common.event

import androidx.compose.runtime.Composable
import com.puzzle.designsystem.component.SnackBarType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventHelper @Inject constructor() {
    private val _eventFlow = Channel<PieceEvent>(BUFFERED)
    val eventFlow = _eventFlow.receiveAsFlow()

    fun sendEvent(event: PieceEvent) {
        _eventFlow.trySend(event)
    }
}

sealed class PieceEvent {
    data class ShowSnackBar(
        val msg: String,
        val type: SnackBarType = SnackBarType.Info
    ) : PieceEvent()

    data object HideSnackBar : PieceEvent()
    data class ShowBottomSheet(val content: @Composable () -> Unit) : PieceEvent()
    data object HideBottomSheet : PieceEvent()
}
