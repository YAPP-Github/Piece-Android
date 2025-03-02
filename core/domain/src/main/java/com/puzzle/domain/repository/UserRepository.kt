package com.puzzle.domain.repository

import com.puzzle.domain.model.user.RejectReason
import com.puzzle.domain.model.user.UserInfo
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.model.user.UserSetting
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface UserRepository {
    fun getUserRole(): Flow<UserRole>
    suspend fun getRejectReason(): Result<RejectReason>
    suspend fun getUserSettingInfo(): Result<UserSetting>
    suspend fun getBlockSyncTime(): Result<LocalDateTime>
    suspend fun getUserInfo(): Result<UserInfo>
    suspend fun updatePushNotification(toggle: Boolean): Result<Unit>
    suspend fun updateMatchNotification(toggle: Boolean): Result<Unit>
    suspend fun updateBlockAcquaintances(toggle: Boolean): Result<Unit>
}
