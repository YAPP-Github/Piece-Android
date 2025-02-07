package com.puzzle.data.repository

import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.user.UserRole
import com.puzzle.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) : UserRepository {
    override suspend fun getUserRole(): Result<UserRole> = runCatching {
        val userRoleString = localUserDataSource.userRole.first()
        UserRole.create(userRoleString)
    }
}
