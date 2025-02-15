package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.domain.model.configure.ForceUpdate
import com.puzzle.domain.repository.ConfigureRepository
import com.puzzle.network.model.configure.GetForceUpdateInfoResponse
import com.puzzle.network.source.configure.ConfigDataSource
import javax.inject.Inject

class ConfigureRepositoryImpl @Inject constructor(
    private val configureDataSource: ConfigDataSource,
) : ConfigureRepository {
    override suspend fun getForceUpdateMinVersion(): Result<ForceUpdate> = suspendRunCatching {
        configureDataSource.getReferenceType(
            key = ConfigDataSource.FORCE_UPDATE,
            defaultValue = GetForceUpdateInfoResponse(),
        ).toDomain()
    }
}
