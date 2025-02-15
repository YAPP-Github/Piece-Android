package com.puzzle.network.source.auth

import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.VerifyAuthCodeResponse

interface AuthDataSource {
    suspend fun loginOauth(provider: OAuthProvider, oauthCredential: String): Result<LoginOauthResponse>
    suspend fun requestAuthCode(phoneNumber: String): Result<Unit>
    suspend fun verifyAuthCode(phoneNumber: String, code: String): Result<VerifyAuthCodeResponse>
    suspend fun checkTokenHealth(token: String): Result<Unit>
}
