package com.puzzle.domain.repository

interface ConfigureRepository {
    suspend fun getForceUpdateMinVersion(): Result<String>
}
