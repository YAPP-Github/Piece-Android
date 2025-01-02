package com.puzzle.common.event

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
    data class ShowSnackbar(val msg: String) : PieceEvent()
}
