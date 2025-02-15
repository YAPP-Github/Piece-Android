package com.puzzle.domain.repository

import com.puzzle.domain.model.user.RejectReason
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.model.user.UserSetting
import java.time.LocalDateTime

interface UserRepository {
    suspend fun getUserRole(): Result<UserRole>
    suspend fun getRejectReason(): Result<RejectReason>
    suspend fun getUserSettingInfo(): Result<UserSetting>
    suspend fun getBlockSyncTime(): Result<LocalDateTime>
    suspend fun updatePushNotification(toggle: Boolean): Result<Unit>
    suspend fun updateMatchNotification(toggle: Boolean): Result<Unit>
    suspend fun updateBlockAcquaintances(toggle: Boolean): Result<Unit>
}
