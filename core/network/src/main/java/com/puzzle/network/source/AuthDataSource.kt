package com.puzzle.network.source

import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.auth.LoginOauthRequest
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.RequestAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.model.getResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun loginOauth(provider: OAuthProvider, token: String): Result<LoginOauthResponse> =
        pieceApi.loginOauth(
            LoginOauthRequest(
                providerName = provider.apiValue,
                token = token,
            )
        ).getResult()

    suspend fun requestAuthCode(phoneNumber: String): Result<Unit> =
        pieceApi.requestAuthCode(RequestAuthCodeRequest(phoneNumber))
            .getResult()

    suspend fun verifyAuthCode(phoneNumber: String, code: String): Result<VerifyAuthCodeResponse> =
        pieceApi.verifyAuthCode(
            VerifyAuthCodeRequest(
                phoneNumber = phoneNumber,
                code = code,
            )
        ).getResult()
}
