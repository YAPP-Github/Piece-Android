package com.puzzle.datastore.datasource.token

import kotlinx.coroutines.flow.Flow

interface LocalTokenDataSource {
    val accessToken: Flow<String>
    val refreshToken: Flow<String>
    suspend fun setAccessToken(accessToken: String)
    suspend fun setRefreshToken(refreshToken: String)
    suspend fun clearToken()
}
