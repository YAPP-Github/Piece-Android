package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.datastore.datasource.LocalTokenDataSource
import com.puzzle.datastore.datasource.LocalUserDataSource
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.network.source.AuthDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : AuthRepository {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        token: String
    ): Result<Unit> = suspendRunCatching {
        val response = authDataSource.loginOauth(oAuthProvider, token).getOrThrow()

        coroutineScope {
            val accessTokenJob = launch {
                response.accessToken?.let { localTokenDataSource.setAccessToken(it) }
            }
            val refreshTokenJob = launch {
                response.refreshToken?.let { localTokenDataSource.setRefreshToken(it) }
            }
            val userRoleJob = launch {
                response.role?.let { localUserDataSource.setUserRole(it) }
            }

            accessTokenJob.join()
            refreshTokenJob.join()
            userRoleJob.join()
        }
    }
}
