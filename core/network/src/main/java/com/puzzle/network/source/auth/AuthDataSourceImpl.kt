package com.puzzle.network.source.auth

import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.auth.LoginOauthRequest
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.RequestAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val pieceApi: PieceApi,
) : AuthDataSource {
    override suspend fun loginOauth(
        provider: OAuthProvider,
        oauthCredential: String
    ): Result<LoginOauthResponse> =
        pieceApi.loginOauth(
            LoginOauthRequest(
                providerName = provider.apiValue,
                oauthCredential = oauthCredential,
            )
        ).unwrapData()

    override suspend fun requestAuthCode(phoneNumber: String): Result<Unit> =
        pieceApi.requestAuthCode(RequestAuthCodeRequest(phoneNumber))
            .unwrapData()

    override suspend fun verifyAuthCode(
        phoneNumber: String,
        code: String
    ): Result<VerifyAuthCodeResponse> =
        pieceApi.verifyAuthCode(
            VerifyAuthCodeRequest(
                phoneNumber = phoneNumber,
                code = code,
            )
        ).unwrapData()

    override suspend fun checkTokenHealth(token: String): Result<Unit> =
        pieceApi.checkTokenHealth(token).unwrapData()
}
