package com.puzzle.network.model.user

import com.puzzle.domain.model.user.ProfileStatus
import com.puzzle.domain.model.user.UserInfo
import com.puzzle.domain.model.user.UserRole
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoResponse(
    val userId: String?,
    val profileStatus: String?,
    val role: String?,
) {
    fun toDomain() = UserInfo(
        userId = userId ?: UNKNOWN_STRING,
        userRole = UserRole.create(role),
        profileStatus = ProfileStatus.create(role),
    )
}
