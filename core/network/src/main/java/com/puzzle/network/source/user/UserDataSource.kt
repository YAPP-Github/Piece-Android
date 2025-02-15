package com.puzzle.network.source.user

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.unwrapData
import com.puzzle.network.model.user.GetBlockSyncTimeResponse
import com.puzzle.network.model.user.GetSettingInfoResponse
import com.puzzle.network.model.user.UpdateSettingRequest
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun getSettingsInfo(): Result<GetSettingInfoResponse> =
        pieceApi.getSettingInfos().unwrapData()

    suspend fun updatePushNotification(toggle: Boolean): Result<Unit> =
        pieceApi.updatePushNotification(UpdateSettingRequest(toggle)).unwrapData()

    suspend fun updateMatchNotification(toggle: Boolean): Result<Unit> =
        pieceApi.updateMatchNotification(UpdateSettingRequest(toggle)).unwrapData()

    suspend fun updateBlockAcquaintances(toggle: Boolean): Result<Unit> =
        pieceApi.updateBlockAcquaintances(UpdateSettingRequest(toggle)).unwrapData()

    suspend fun getBlockSyncTime(): Result<GetBlockSyncTimeResponse> =
        pieceApi.getBlockSyncTime().unwrapData()
}
