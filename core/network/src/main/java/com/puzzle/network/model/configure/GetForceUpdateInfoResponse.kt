package com.puzzle.network.model.configure

import com.puzzle.domain.model.configure.ForceUpdate
import kotlinx.serialization.Serializable

@Serializable
data class GetForceUpdateInfoResponse(
    val minVersion: String = "1.0.0",
) {
    fun toDomain() = ForceUpdate(
        minVersion = minVersion,
    )
}
