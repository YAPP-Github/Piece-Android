package com.puzzle.network.api.sse

import android.util.Log
import com.launchdarkly.eventsource.MessageEvent
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.network.model.profile.SseAiSummaryResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SseEventHandler @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val json: Json,
) : BackgroundEventHandler {
    private val _sseChannel = Channel<SseAiSummaryResponse>(BUFFERED)
    val sseChannel = _sseChannel.receiveAsFlow()

    override fun onOpen() {
        Log.d("SseEventHandler", "SSE를 연결하였습니다.")
    }

    override fun onClosed() {
        Log.d("SseEventHandler", "SSE를 연결을 종료하였습니다.")
    }

    override fun onMessage(event: String, messageEvent: MessageEvent) {
        val eventId = messageEvent.lastEventId
        val eventType = messageEvent.eventName
        val eventData = messageEvent.data

        Log.d("SseEventHandler", "Event ID: $eventId")
        Log.d("SseEventHandler", "Event Type: $eventType")
        Log.d("SseEventHandler", "Event Data: $eventData")

        when (eventType) {
            SseEventType.PROFILE_TALK_SUMMARY_MESSAGE.name -> {
                try {
                    val response = json.decodeFromString<SseAiSummaryResponse>(eventData)
                    _sseChannel.trySend(response)
                } catch (e: SerializationException) {
                    runBlocking { errorHelper.sendError(e) }
                }
            }

            else -> Unit
        }
    }

    override fun onComment(comment: String) {
        throw UnsupportedOperationException("사용하지 않는 함수입니다.")
    }

    override fun onError(t: Throwable) {
        // SSE 연결 전 또는 후 오류 발생시 처리 로직 작성

        // 서버가 2XX 이외의 오류 응답시 com.launchdarkly.eventsource.StreamHttpErrorException: Server returned HTTP error 401 예외가 발생
        // 클라이언트에서 서버의 연결 유지 시간보다 짧게 설정시 error=com.launchdarkly.eventsource.StreamIOException: java.net.SocketTimeoutException: timeout 예외가 발생
        // 서버가 연결 유지 시간 초과로 종료시 error=com.launchdarkly.eventsource.StreamClosedByServerException: Stream closed by server 예외가 발생
    }

    enum class SseEventType {
        PROFILE_TALK_SUMMARY_MESSAGE;
    }
}
