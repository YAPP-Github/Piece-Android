package com.puzzle.data.repository

import android.util.Log
import com.puzzle.common.suspendRunCatching
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun loginOauth(oAuthProvider: OAuthProvider, token: String): Result<Unit> =
        suspendRunCatching {
            val result = authDataSource.loginOauth(oAuthProvider, token)
            Log.d("test", result.getOrDefault("").toString())
            Result.success(Unit)
        }
}
