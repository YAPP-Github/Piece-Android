package com.puzzle.domain.repository

import com.puzzle.domain.model.configure.ForceUpdate

interface ConfigureRepository {
    suspend fun getForceUpdateMinVersion(): Result<ForceUpdate>
}
