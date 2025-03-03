package com.puzzle.presentation.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.puzzle.designsystem.R
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.NotificationRepository
import com.puzzle.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var errorHelper: ErrorHelper

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            notificationRepository.updateDeviceToken(token)
                .onFailure { error -> errorHelper.sendError(error) }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Piece"
        val body = message.notification?.body ?: ""
        val data = message.data

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        if (data.isNotEmpty()) {
            data.forEach { (key, value) -> intent.putExtra(key, value) }
        }

        // 수신한 알림 읽음 처리
        data["id"]?.let { id ->
            scope.launch {
                notificationRepository.readNotification(id)
                    .onFailure { errorHelper.sendError(it) }
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(context, BACKGROUND_CHANNEL)
            .setSmallIcon(R.drawable.ic_alarm)
            .setColor(ContextCompat.getColor(context, R.color.primary))
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val BACKGROUND_CHANNEL = "백그라운드 알림"
        const val BACKGROUND_CHANNEL_DESCRIPTION = "백그라운드 알림을 관리하는 채널입니다."
    }
}
