package com.puzzle.network.api

import com.puzzle.network.model.ApiResponse
import com.puzzle.network.model.auth.LoginOauthRequest
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.RequestAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.model.matching.BlockContactsRequest
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.matching.ReportUserRequest
import com.puzzle.network.model.profile.UploadProfileRequest
import com.puzzle.network.model.profile.UploadProfileResponse
import com.puzzle.network.model.terms.AgreeTermsRequest
import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.token.RefreshTokenRequest
import com.puzzle.network.model.token.RefreshTokenResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun loadValuePickQuestions(): Result<ApiResponse<LoadValuePicksResponse>>

    @GET("/api/valueTalks")
    suspend fun loadValueTalkQuestions(): Result<ApiResponse<LoadValueTalksResponse>>

    @GET("/api/login/token/health-check")
    suspend fun checkTokenHealth(@Query("token") token: String): Result<ApiResponse<Unit>>

    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Result<ApiResponse<RefreshTokenResponse>>

    @POST("/api/profiles/check-nickname")
    suspend fun checkNickname(@Query("nickname") nickname: String): Result<ApiResponse<Boolean>>

    @POST("/api/profiles")
    suspend fun uploadProfile(@Body uploadProfileRequest: UploadProfileRequest): Result<ApiResponse<UploadProfileResponse>>

    @Multipart
    @POST("/api/profiles/images")
    suspend fun uploadProfileImage(@Part file: MultipartBody.Part): Result<ApiResponse<String>>

    @POST("/api/terms/agree")
    suspend fun agreeTerms(@Body agreeTermsRequest: AgreeTermsRequest): Result<ApiResponse<Unit>>

    @POST("/api/reports")
    suspend fun reportUser(@Body reportUserRequest: ReportUserRequest): Result<ApiResponse<Unit>>

    @POST("/api/matches/blocks/users/{userId}")
    suspend fun blockUser(@Path("userId") userId: Int): Result<ApiResponse<Unit>>

    @POST("/api/blockContacts")
    suspend fun blockContacts(@Body blockContactsRequest: BlockContactsRequest): Result<ApiResponse<Unit>>

    @GET("/api/matches/infos")
    suspend fun getMatchInfo(): Result<ApiResponse<GetMatchInfoResponse>>

    @PATCH("/api/matches/pieces/check")
    suspend fun checkMatchingPiece(): Result<ApiResponse<Unit>>

    @POST("/api/matches/accept")
    suspend fun acceptMatching(): Result<ApiResponse<Unit>>
}
