package com.puzzle.domain.model.user

enum class UserRole {
    // SMS 인증전
    NONE,

    // 프로필 등록전
    REGISTER,

    // 아직 심사중
    PENDING,

    // 심사까지 마침
    USER;

    companion object {
        fun create(value: String): UserRole {
            return UserRole.entries.firstOrNull { it.name == value } ?: NONE
        }
    }
}
