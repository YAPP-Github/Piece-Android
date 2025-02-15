package com.puzzle.data.fake.source.matching

import com.puzzle.network.model.matching.GetContactsResponse
import com.puzzle.network.model.matching.GetMatchInfoResponse
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse
import com.puzzle.network.source.matching.MatchingDataSource

class FakeMatchingDataSource : MatchingDataSource {
    private var opponentProfileBasicData: GetOpponentProfileBasicResponse? = null
    private var opponentValuePicksData: GetOpponentValuePicksResponse? = null
    private var opponentValueTalksData: GetOpponentValueTalksResponse? = null
    private var opponentProfileImageData: GetOpponentProfileImageResponse? = null

    fun setOpponentProfileData(
        basicData: GetOpponentProfileBasicResponse,
        valuePicksData: GetOpponentValuePicksResponse,
        valueTalksData: GetOpponentValueTalksResponse,
        profileImageData: GetOpponentProfileImageResponse
    ) {
        opponentProfileBasicData = basicData
        opponentValuePicksData = valuePicksData
        opponentValueTalksData = valueTalksData
        opponentProfileImageData = profileImageData
    }

    override suspend fun getOpponentProfileBasic(): Result<GetOpponentProfileBasicResponse> =
        Result.success(
            opponentProfileBasicData ?: GetOpponentProfileBasicResponse(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        )

    override suspend fun getOpponentValuePicks(): Result<GetOpponentValuePicksResponse> =
        Result.success(
            opponentValuePicksData ?: GetOpponentValuePicksResponse(
                null,
                null,
                null,
                null
            )
        )

    override suspend fun getOpponentValueTalks(): Result<GetOpponentValueTalksResponse> =
        Result.success(
            opponentValueTalksData ?: GetOpponentValueTalksResponse(
                null,
                null,
                null,
                null
            )
        )

    override suspend fun getOpponentProfileImage(): Result<GetOpponentProfileImageResponse> =
        Result.success(opponentProfileImageData ?: GetOpponentProfileImageResponse(null))

    override suspend fun reportUser(userId: Int, reason: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun blockUser(userId: Int): Result<Unit> = Result.success(Unit)

    override suspend fun blockContacts(phoneNumbers: List<String>): Result<Unit> =
        Result.success(Unit)

    override suspend fun getMatchInfo(): Result<GetMatchInfoResponse> = Result.success(
        GetMatchInfoResponse(
            matchId = 1234,
            matchStatus = "WAITING",
            description = "안녕하세요, 저는 음악과 여행을 좋아하는 사람입니다.",
            nickname = "여행하는음악가",
            birthYear = "1995",
            location = "서울특별시",
            job = "음악 프로듀서",
            matchedValueCount = 3,
            matchedValueList = listOf("음악", "여행", "독서")
        )
    )

    override suspend fun getContacts(): Result<GetContactsResponse> {
        return Result.success(GetContactsResponse())
    }

    override suspend fun checkMatchingPiece(): Result<Unit> = Result.success(Unit)

    override suspend fun acceptMatching(): Result<Unit> = Result.success(Unit)
}
