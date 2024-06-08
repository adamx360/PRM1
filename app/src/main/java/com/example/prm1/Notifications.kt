package com.example.prm1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.prm1.service.AlertService

private const val REQUEST_CODE = 10
private const val CHANNEL_ID = "ID_CHANNEL_DEFAULT"

object Notifications {
    fun createChannel(context: Context) {
        val channel =
            NotificationChannel(CHANNEL_ID, "Kwarantanna", NotificationManager.IMPORTANCE_LOW)
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun createNotification(context: Context) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Alarm!")
        .addAction(0, "Check again", createIntent(context))
        .build()

    private fun createIntent(context: Context): PendingIntent =
        PendingIntent.getForegroundService(
            context, REQUEST_CODE,
            Intent(context, AlertService::class.java)
                .putExtra("check", true),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
}