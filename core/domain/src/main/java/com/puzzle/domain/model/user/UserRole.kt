package com.puzzle.domain.model.user

enum class UserRole {
    NONE,
    REGISTER,
    PENDING,
    USER;

    companion object {
        fun create(value: String): UserRole {
            return UserRole.entries.firstOrNull { it.name == value } ?: NONE
        }
    }
}
