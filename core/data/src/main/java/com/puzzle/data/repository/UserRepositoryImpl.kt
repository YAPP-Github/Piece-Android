package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.user.RejectReason
import com.puzzle.domain.model.user.UserInfo
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.model.user.UserSetting
import com.puzzle.domain.repository.UserRepository
import com.puzzle.network.model.user.GetBlockSyncTimeResponse
import com.puzzle.network.model.user.GetRejectReasonResponse
import com.puzzle.network.model.user.GetSettingInfoResponse
import com.puzzle.network.model.user.GetUserInfoResponse
import com.puzzle.network.source.user.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override fun getUserRole(): Flow<UserRole> = localUserDataSource
        .userRole
        .map(UserRole::create)

    override suspend fun getRejectReason(): Result<RejectReason> = userDataSource.getRejectReason()
        .map(GetRejectReasonResponse::toDomain)

    override suspend fun getUserSettingInfo(): Result<UserSetting> =
        userDataSource.getSettingsInfo().mapCatching(GetSettingInfoResponse::toDomain)

    override suspend fun getBlockSyncTime(): Result<LocalDateTime> =
        userDataSource.getBlockSyncTime().mapCatching(GetBlockSyncTimeResponse::toDomain)

    override suspend fun getUserInfo(): Result<UserInfo> = suspendRunCatching {
        val userInfo = userDataSource.getUserInfo()
            .mapCatching(GetUserInfoResponse::toDomain)
            .getOrThrow()

        localUserDataSource.setUserRole(userInfo.userRole.toString())
        userInfo
    }

    override suspend fun updatePushNotification(toggle: Boolean): Result<Unit> =
        userDataSource.updatePushNotification(toggle)

    override suspend fun updateMatchNotification(toggle: Boolean): Result<Unit> =
        userDataSource.updateMatchNotification(toggle)

    override suspend fun updateBlockAcquaintances(toggle: Boolean): Result<Unit> =
        userDataSource.updateBlockAcquaintances(toggle)
}
