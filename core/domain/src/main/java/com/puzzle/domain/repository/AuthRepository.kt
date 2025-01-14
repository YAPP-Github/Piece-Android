package com.puzzle.domain.repository

import com.puzzle.domain.model.auth.OAuthProvider

interface AuthRepository {
    suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        token: String
    ): Result<Unit>
}
