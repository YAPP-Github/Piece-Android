package com.puzzle.data.repository

import com.puzzle.common.suspendRunCatching
import com.puzzle.datastore.datasource.token.LocalTokenDataSource
import com.puzzle.datastore.datasource.user.LocalUserDataSource
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.network.source.auth.AuthDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : AuthRepository {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): Result<Unit> = suspendRunCatching {
        val response = authDataSource.loginOauth(oAuthProvider, oauthCredential).getOrThrow()

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

    override suspend fun logout(): Result<Unit> = suspendRunCatching {
        coroutineScope {
            val clearUserRoleJob = launch { localUserDataSource.clearUserRole() }
            val clearTokenJob = launch { localTokenDataSource.clearToken() }

            clearTokenJob.join()
            clearUserRoleJob.join()
        }
    }

    override suspend fun checkTokenHealth(): Result<Unit> = suspendRunCatching {
        val accessToken = localTokenDataSource.accessToken.first()
        authDataSource.checkTokenHealth(accessToken).getOrThrow()
    }

    override suspend fun requestAuthCode(phoneNumber: String): Result<Unit> =
        authDataSource.requestAuthCode(phoneNumber)

    override suspend fun verifyAuthCode(phoneNumber: String, code: String): Result<Unit> =
        suspendRunCatching {
            val response = authDataSource.verifyAuthCode(
                phoneNumber = phoneNumber,
                code = code,
            ).getOrThrow()

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
