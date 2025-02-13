package com.puzzle.network.source.user

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.unwrapData
import com.puzzle.network.model.user.GetSettingInfoResponse
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun getSettingsInfo(): Result<GetSettingInfoResponse> =
        pieceApi.getSettingInfos().unwrapData()
}
