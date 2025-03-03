package com.puzzle.piece

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.airbnb.mvrx.Mavericks
import com.kakao.sdk.common.KakaoSdk
import com.puzzle.presentation.notification.NotificationService.Companion.BACKGROUND_CHANNEL
import com.puzzle.presentation.notification.NotificationService.Companion.BACKGROUND_CHANNEL_DESCRIPTION
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PieceApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initNotification()
        initMavericks()
        initKakao()
    }

    private fun initNotification() {
        val channelId = BACKGROUND_CHANNEL
        val channelName = BACKGROUND_CHANNEL
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = BACKGROUND_CHANNEL_DESCRIPTION
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun initMavericks() = Mavericks.initialize(this)
    private fun initKakao() = KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
}
