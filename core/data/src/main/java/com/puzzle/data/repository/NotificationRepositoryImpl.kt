package com.puzzle.data.repository

import com.puzzle.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {
    override suspend fun updateDeviceToken(token: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun readNotification(id: String): Result<Unit> {
        return Result.success(Unit)
    }
}
