package com.example.custommaplocationserviceapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat

object MyNotificationManager {
    const val NOTIFICATION_ID = 9999999

    private const val CHANNEL_LOCATIONS = "channel_locations"

    fun getNotification(context: Context): Notification {
        val activityPendingIntent = PendingIntent.getActivity(context, 0,
            Intent(context, MainActivity::class.java), 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_LOCATIONS)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setContentTitle("You are on Be")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSound(null)
            .setContentIntent(activityPendingIntent)
            .setVibrate(longArrayOf(0))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.abc_ic_ab_back_material))
            .setShowWhen(false)

        return builder.build()
    }

    fun getChannel(context: Context): NotificationChannel? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_LOCATIONS,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                setSound(null, null)
                vibrationPattern = longArrayOf(0)
                setShowBadge(false)
            }
        } else null
}