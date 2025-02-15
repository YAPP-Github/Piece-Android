package com.puzzle.data.fake.source.auth

import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.source.auth.AuthDataSource

class FakeAuthDataSource : AuthDataSource {
    private var loginResponse = LoginOauthResponse("NONE", "accessToken", "refreshToken")
    private var verifyResponse = VerifyAuthCodeResponse("REGISTER", "accessToken", "refreshToken")

    override suspend fun loginOauth(
        provider: OAuthProvider,
        oauthCredential: String
    ): Result<LoginOauthResponse> {
        return Result.success(loginResponse)
    }

    override suspend fun requestAuthCode(phoneNumber: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun verifyAuthCode(
        phoneNumber: String,
        code: String
    ): Result<VerifyAuthCodeResponse> {
        return Result.success(verifyResponse)
    }

    override suspend fun checkTokenHealth(token: String): Result<Unit> {
        return Result.success(Unit)
    }
}
