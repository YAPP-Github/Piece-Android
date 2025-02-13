package com.puzzle.domain.repository

import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.model.user.UserSetting

interface UserRepository {
    suspend fun getUserRole(): Result<UserRole>
    suspend fun getUserSettingInfo(): Result<UserSetting>
}
