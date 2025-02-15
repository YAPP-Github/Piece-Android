package com.puzzle.domain.repository

import com.puzzle.domain.model.user.RejectReason
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.model.user.UserSetting
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUserRole(): Flow<UserRole>
    suspend fun getUserRole(): Result<UserRole>
    suspend fun getRejectReason(): Result<RejectReason>
    suspend fun getUserSettingInfo(): Result<UserSetting>
    suspend fun updatePushNotification(toggle: Boolean): Result<Unit>
    suspend fun updateMatchNotification(toggle: Boolean): Result<Unit>
    suspend fun updateBlockAcquaintances(toggle: Boolean): Result<Unit>
}
