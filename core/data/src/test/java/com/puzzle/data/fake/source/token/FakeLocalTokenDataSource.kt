package com.puzzle.data.fake.source.token

import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalTokenDataSource : LocalTokenDataSource {
    private var currentAccessToken = ""
    private var currentRefreshToken = ""

    override val accessToken: Flow<String> get() = flowOf(currentAccessToken)
    override val refreshToken: Flow<String> get() = flowOf(currentRefreshToken)

    override suspend fun setAccessToken(accessToken: String) {
        currentAccessToken = accessToken
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        currentRefreshToken = refreshToken
    }

    override suspend fun clearToken() {
        currentAccessToken = ""
        currentRefreshToken = ""
    }
}
