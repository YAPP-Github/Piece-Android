package com.puzzle.data.repository

import com.puzzle.domain.repository.ConfigureRepository
import javax.inject.Inject

class ConfigureRepositoryImpl @Inject constructor() : ConfigureRepository {
    override suspend fun getForceUpdateMinVersion(): Result<String> {
        TODO("Not yet implemented")
    }
}
