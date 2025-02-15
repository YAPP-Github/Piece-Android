package com.puzzle.network.api

import com.puzzle.network.model.ApiResponse
import com.puzzle.network.model.auth.LoginOauthRequest
import com.puzzle.network.model.auth.LoginOauthResponse
import com.puzzle.network.model.auth.RequestAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeRequest
import com.puzzle.network.model.auth.VerifyAuthCodeResponse
import com.puzzle.network.model.matching.BlockContactsRequest
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse
import com.puzzle.network.model.matching.ReportUserRequest
import com.puzzle.network.model.profile.GetMyProfileBasicResponse
import com.puzzle.network.model.profile.GetMyValuePicksResponse
import com.puzzle.network.model.profile.GetMyValueTalksResponse
import com.puzzle.network.model.profile.LoadValuePickQuestionsResponse
import com.puzzle.network.model.profile.LoadValueTalkQuestionsResponse
import com.puzzle.network.model.profile.UpdateAiSummaryRequest
import com.puzzle.network.model.profile.UpdateMyProfileBasicRequest
import com.puzzle.network.model.profile.UpdateMyValuePickRequests
import com.puzzle.network.model.profile.UpdateMyValueTalkRequests
import com.puzzle.network.model.profile.UploadProfileRequest
import com.puzzle.network.model.profile.UploadProfileResponse
import com.puzzle.network.model.terms.AgreeTermsRequest
import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.token.RefreshTokenRequest
import com.puzzle.network.model.token.RefreshTokenResponse
import com.puzzle.network.model.user.GetBlockSyncTimeResponse
import com.puzzle.network.model.user.GetSettingInfoResponse
import com.puzzle.network.model.user.UpdateSettingRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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
    suspend fun loadValuePickQuestions(): Result<ApiResponse<LoadValuePickQuestionsResponse>>

    @GET("/api/valueTalks")
    suspend fun loadValueTalkQuestions(): Result<ApiResponse<LoadValueTalkQuestionsResponse>>

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

    @GET("/api/profiles/valueTalks")
    suspend fun getMyValueTalks(): Result<ApiResponse<GetMyValueTalksResponse>>

    @GET("/api/profiles/valuePicks")
    suspend fun getMyValuePicks(): Result<ApiResponse<GetMyValuePicksResponse>>

    @GET("/api/profiles/basic")
    suspend fun getMyProfileBasic(): Result<ApiResponse<GetMyProfileBasicResponse>>

    @PUT("/api/profiles/valueTalks")
    suspend fun updateMyValueTalks(@Body updateMyValueTalkRequests: UpdateMyValueTalkRequests): Result<ApiResponse<GetMyValueTalksResponse>>

    @PUT("/api/profiles/valuePicks")
    suspend fun updateMyValuePicks(@Body updateMyValuePickRequests: UpdateMyValuePickRequests): Result<ApiResponse<GetMyValuePicksResponse>>

    @PUT("/api/profiles/basic")
    suspend fun updateMyProfileBasic(@Body updateMyProfileBasicRequest: UpdateMyProfileBasicRequest): Result<ApiResponse<GetMyProfileBasicResponse>>

    @PATCH("/api/profiles/valueTalks/{profileTalkId}/summary")
    suspend fun updateAiSummary(
        @Path("profileTalkId") id: Int,
        @Body updateAiSummaryRequest: UpdateAiSummaryRequest,
    ): Result<ApiResponse<Unit>>

    @GET("/api/matches/infos")
    suspend fun getMatchInfo(): Result<ApiResponse<GetMatchInfoResponse>>

    @GET("/api/matches/values/talks")
    suspend fun getOpponentValueTalks(): Result<ApiResponse<GetOpponentValueTalksResponse>>

    @GET("/api/matches/values/picks")
    suspend fun getOpponentValuePicks(): Result<ApiResponse<GetOpponentValuePicksResponse>>

    @GET("/api/matches/profiles/basic")
    suspend fun getOpponentProfileBasic(): Result<ApiResponse<GetOpponentProfileBasicResponse>>

    @GET("/api/matches/images")
    suspend fun getOpponentProfileImage(): Result<ApiResponse<GetOpponentProfileImageResponse>>

    @PATCH("/api/matches/pieces/check")
    suspend fun checkMatchingPiece(): Result<ApiResponse<Unit>>

    @POST("/api/matches/accept")
    suspend fun acceptMatching(): Result<ApiResponse<Unit>>

    @GET("/api/settings/infos")
    suspend fun getSettingInfos(): Result<ApiResponse<GetSettingInfoResponse>>

    @PUT("/api/settings/notification")
    suspend fun updatePushNotification(@Body updateSettingRequest: UpdateSettingRequest): Result<ApiResponse<Unit>>

    @PUT("/api/settings/notification/match")
    suspend fun updateMatchNotification(@Body updateSettingRequest: UpdateSettingRequest): Result<ApiResponse<Unit>>

    @PUT("/api/settings/block/acquaintance")
    suspend fun updateBlockAcquaintances(@Body updateSettingRequest: UpdateSettingRequest): Result<ApiResponse<Unit>>

    @GET("/api/settings/blocks/contacts/sync-time")
    suspend fun getBlockSyncTime(): Result<ApiResponse<GetBlockSyncTimeResponse>>
}
