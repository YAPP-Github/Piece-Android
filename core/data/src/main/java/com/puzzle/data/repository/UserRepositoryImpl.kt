package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.model.user.UserSetting
import com.puzzle.domain.repository.UserRepository
import com.puzzle.network.model.user.GetSettingInfoResponse
import com.puzzle.network.source.user.UserDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun getUserRole(): Result<UserRole> = suspendRunCatching {
        val userRoleString = localUserDataSource.userRole.first()
        UserRole.create(userRoleString)
    }

    override suspend fun getUserSettingInfo(): Result<UserSetting> =
        userDataSource.getSettingsInfo().mapCatching(GetSettingInfoResponse::toDomain)
}
