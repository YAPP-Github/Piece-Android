package com.puzzle.domain.repository

import com.puzzle.domain.model.user.UserRole

interface UserRepository {
    suspend fun getUserRole(): Result<UserRole>
}
