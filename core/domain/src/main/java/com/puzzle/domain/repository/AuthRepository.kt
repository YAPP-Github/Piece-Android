package com.puzzle.domain.repository

import com.puzzle.domain.model.auth.OAuthProvider

interface AuthRepository {
    suspend fun requestAuthCode(phoneNumber: String): Result<Boolean>
    suspend fun verifyAuthCode(code: String): Result<Boolean>
    suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        token: String
    ): Result<Unit>
}
