package com.puzzle.network.api

import com.puzzle.network.model.ApiResponse
import com.puzzle.network.model.auth.LoginOauthRequest
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.RequestAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.token.RefreshTokenRequest
import com.puzzle.network.model.token.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PieceApi {
    @POST("/api/login/oauth")
    suspend fun loginOauth(@Body loginOauthRequest: LoginOauthRequest): Result<ApiResponse<LoginOauthResponse>>

    @POST("/api/register/sms/auth/code")
    suspend fun requestAuthCode(@Body requestAuthCodeRequest: RequestAuthCodeRequest): Result<ApiResponse<Unit>>

    @POST("/api/register/sms/auth/code/verify")
    suspend fun verifyAuthCode(@Body verifyAuthCodeRequest: VerifyAuthCodeRequest): Result<ApiResponse<VerifyAuthCodeResponse>>

    @GET("/api/terms")
    suspend fun loadTerms(): Result<ApiResponse<LoadTermsResponse>>

    @GET("/api/valuePicks")
    suspend fun loadValuePicks(): Result<ApiResponse<LoadValuePicksResponse>>

    @GET("/api/valueTalks")
    suspend fun loadValueTalks(): Result<ApiResponse<LoadValueTalksResponse>>

    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Result<ApiResponse<RefreshTokenResponse>>
}
