package com.puzzle.network.model.user

import com.puzzle.domain.model.user.ProfileStatus
import com.puzzle.domain.model.user.RejectReason
import kotlinx.serialization.Serializable

@Serializable
data class GetRejectReasonResponse(
    val profileStatus: String?,
    val reasonImage: Boolean?,
    val reasonValues: Boolean?,
) {
    fun toDomain(): RejectReason = RejectReason(
        profileStatus = ProfileStatus.fromName(profileStatus),
        reasonImage = reasonImage ?: false,
        reasonValues = reasonValues ?: false,
    )
}
