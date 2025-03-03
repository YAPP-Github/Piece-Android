package com.puzzle.domain.repository

import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.domain.model.user.UserRole

interface AuthRepository {
    suspend fun requestAuthCode(phoneNumber: String): Result<Unit>
    suspend fun verifyAuthCode(
        phoneNumber: String,
        code: String
    ): Result<Unit>

    suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): Result<UserRole>

    suspend fun logout(): Result<Unit>
    suspend fun withdraw(reason: String): Result<Unit>

    suspend fun checkTokenHealth(): Result<Unit>
}
