package com.puzzle.domain.model.user

data class UserInfo(
    val userId: String,
    val userRole: UserRole,
    val profileStatus: ProfileStatus,
)
