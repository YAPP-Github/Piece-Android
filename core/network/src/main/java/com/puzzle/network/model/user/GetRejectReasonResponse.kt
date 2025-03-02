package com.puzzle.network.model.user

import com.puzzle.domain.model.user.RejectReason
import kotlinx.serialization.Serializable

@Serializable
data class GetRejectReasonResponse(
    val reasonImage: Boolean?,
    val reasonValues: Boolean?,
) {
    fun toDomain(): RejectReason = RejectReason(
        reasonImage = reasonImage ?: false,
        reasonValues = reasonValues ?: false,
    )
}
