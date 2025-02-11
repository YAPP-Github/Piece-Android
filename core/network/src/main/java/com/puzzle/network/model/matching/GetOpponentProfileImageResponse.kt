package com.puzzle.network.model.matching

import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class GetOpponentProfileImageResponse(
    val imageUrl: String?,
) {
    fun toDomain(): String = imageUrl ?: UNKNOWN_STRING
}
