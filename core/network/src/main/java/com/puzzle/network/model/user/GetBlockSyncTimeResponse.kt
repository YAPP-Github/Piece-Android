package com.puzzle.network.model.user

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
data class GetBlockSyncTimeResponse(
    val syncTime: String?,
) {
    fun toDomain(): LocalDateTime = syncTime?.let {
        LocalDateTime.parse(it, DateTimeFormatter.ISO_INSTANT)
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime()
    } ?: LocalDateTime.MIN
}
